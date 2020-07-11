package com.github.DenFade.gdlistener;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.github.DenFade.gdlistener.event.AbstractEvent;
import com.github.DenFade.gdlistener.event.AwardedLevelUpdatedEvent;
import com.github.DenFade.gdlistener.utils.FileStream;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class EventLoopService extends Service {

    private final Timer timer = new Timer();
    private EventLoop loop;
    private boolean toggleToast = false;

    public EventLoopService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        startForeground(1, new NotificationCompat.Builder(this, getString(R.string.channelId))
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("EventLoopService is now running...")
                .setContentText("Started At: " + new Date().toString())
                .setPriority(NotificationCompat.PRIORITY_HIGH).build()
        );

        Collection<AbstractEvent<?>> list = new ArrayList<>();
        int period;

        try{
            Properties setting = new Properties();
            setting.load(Files.newBufferedReader(Paths.get(FileStream.ROOT_DIR + "loop.properties")));
            period = Integer.parseInt(setting.getProperty("delay"));
            if(setting.getProperty("awarded").equals("1")) list.add(new AwardedLevelUpdatedEvent());
            if(setting.getProperty("withToast").equals("1")) toggleToast = true;
            loop = new EventLoop(list);
        } catch (Exception e){
            period = 30_000;
            list.add(new AwardedLevelUpdatedEvent());
            toggleToast = false;
            loop = new EventLoop(list);
            e.printStackTrace();
        }

        Log.d("Timer", "schedule 호출전");
        timer.schedule(loop, 5000, period);
        Log.d("Timer", "schedule 호출");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Timer", "cancel 호출: "+loop.cancel());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class EventLoop extends TimerTask {

        private Handler handler;
        private Collection<AbstractEvent<?>> events;

        EventLoop(Collection<AbstractEvent<?>> events){
            this.events = events;
        }

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public void run() {
            Log.d("Event", "start event!");
            Handler handler = new Handler(Looper.getMainLooper());

            try{
                Gson gson = new Gson();
                for(AbstractEvent event : events){
                    event.dbInit(); //when db not exists
                    List items = event.run();
                    if(items == null) return;
                    List<Long> alive;
                    try {
                        JsonElement db = event.dbLoad();
                        if(db == null){
                            Log.d("DB", "oof! empty db");
                            return;
                        }
                        Type aliveType = new TypeToken<List<Long>>(){}.getType();
                        alive = gson.fromJson(db.getAsJsonObject().get("alive"), aliveType);
                    } catch (Exception e){
                        Log.d("DB", "Failed to get alive list");
                        return;
                    }
                    List updated = event.filter(alive, items);
                    event.dbUpdateAndNotify(updated, EventLoopService.this, createNotificationChannel());
                }
                if(toggleToast){
                    handler.post(() -> Toast.makeText(EventLoopService.this, "Event: Loop successfully", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e){
                if(toggleToast){
                    handler.post(() -> Toast.makeText(EventLoopService.this, "Event: An error occurred\n" + e.getClass().getName() + ": " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
                }
                e.printStackTrace();
            }
            Log.d("Event", "end event!");
        }
    }

    private NotificationManager createNotificationChannel(){
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        CharSequence name = getString(R.string.channelName);
        String description = getString(R.string.channelDesc);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(getString(R.string.channelId), name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
        return notificationManager;
    }
}
