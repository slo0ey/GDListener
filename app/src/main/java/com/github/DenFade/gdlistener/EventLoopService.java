package com.github.DenFade.gdlistener;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.github.DenFade.gdlistener.event.AbstractEvent;
import com.github.DenFade.gdlistener.event.AwardedLevelUpdatedEvent;
import com.github.DenFade.gdlistener.utils.FileUtils;

import org.json.JSONException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;

public class EventLoopService extends Service {

    final Timer timer = new Timer();
    EventLoop loop;

    public EventLoopService() {
    }

    IEventLoopService.Stub binder = new IEventLoopService.Stub() {
        @Override
        public String getJson() throws RemoteException {
            return "";
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        Collection<AbstractEvent<?>> list = new ArrayList<>();
        int period;
        try{
            Properties setting = new Properties();
            setting.load(Files.newBufferedReader(Paths.get(FileUtils.ROOT_DIR + "loop.properties")));

            period = Integer.parseInt(setting.getProperty("delay"));

                Log.d("Awarded", String.valueOf(setting.getProperty("awarded") == "1"));

            list.add(new AwardedLevelUpdatedEvent());
            loop = new EventLoop(list);
        } catch (IOException e){
            period = 30_000;
            list.add(new AwardedLevelUpdatedEvent());
            loop = new EventLoop(list);

            e.printStackTrace();
        }
        Log.d("Timer", "schedule 호출전");
        timer.schedule(loop, 10000, period);
        Log.d("Timer", "schedule 호출");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Timer", "cancel 호출: "+String.valueOf(loop.cancel()));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
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
            try{
                Log.d("Event", "start event!");
                Log.d("EventList", events.toString());
                for(AbstractEvent event : events){
                    Log.d("Event", event.toString());
                    event.dbInit(); //when db not exists
                    List items = event.run();
                    if(items == null){
                        Log.d("Failed to fetch", "Gets list as -1");
                        return;
                    }
                    List<Integer> alive;
                    try {
                        alive = (List) event.dbLoad().getJSONArray("alive");
                    } catch (JSONException e){
                        e.printStackTrace();
                        Log.d("Failed to get alive list", Objects.requireNonNull(e.getMessage()));
                        return;
                    }
                    List updated = event.filter(alive, items);
                }
                Log.d("Event", "end event!");
            } catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    /*public void prepare() throws IOException {
        Properties loop = new Properties();
        loop.load(getClass().getResourceAsStream(FileUtils.ROOT_DIR + "loop.properties"));

        loopDelay = Integer.parseInt(loop.getProperty("delay"));
        notification = Integer.parseInt(loop.getProperty("notification"));

        if(loop.getProperty("awarded") == "1") events.add(new AwardedLevelUpdatedEvent());
    }*/
}
