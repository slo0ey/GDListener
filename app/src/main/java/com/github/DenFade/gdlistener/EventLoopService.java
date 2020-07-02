package com.github.DenFade.gdlistener;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;

import com.github.DenFade.gdlistener.event.AbstractEvent;
import com.github.DenFade.gdlistener.event.AwardedLevelUpdatedEvent;
import com.github.DenFade.gdlistener.utils.FileUtils;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDEntity;
import com.github.alex1304.jdash.util.GDPaginator;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class EventLoopService extends Service {
    public EventLoopService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class EventLoop implements Runnable{

        private Handler handler;
        private int loopDelay;
        private boolean notification;
        private final AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
        private Collection<AbstractEvent<?>> events;

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public void run() {
            for(AbstractEvent event : events){
                GDPaginator items = event.run(client);
                List<Integer> alive;
                try {
                    alive = (List) event.dbLoad().getJSONArray("alive");
                } catch (JSONException e){
                    e.printStackTrace();
                    return;
                }
                List newOne = event.filter(alive, items.asList());
                if(notification){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        }

        public void prepare() throws IOException {
            Properties loop = new Properties();
            loop.load(getClass().getResourceAsStream(FileUtils.ROOT_DIR + "loop.properties"));

            loopDelay = Integer.parseInt(loop.getProperty("delay"));
            notification = Boolean.parseBoolean(loop.getProperty("onNoti"));

            if(loop.getProperty("awarded") == "1") events.add(new AwardedLevelUpdatedEvent());
        }
    }
}
