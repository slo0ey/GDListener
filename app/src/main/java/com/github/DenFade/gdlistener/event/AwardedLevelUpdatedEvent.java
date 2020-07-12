package com.github.DenFade.gdlistener.event;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.github.DenFade.gdlistener.R;
import com.github.DenFade.gdlistener.event.scanner.ListUpdatedScanner;
import com.github.DenFade.gdlistener.event.worker.AwardedLevelAddEventWorker;
import com.github.DenFade.gdlistener.gd.entity.GDLevel;
import com.github.DenFade.gdlistener.gd.log.GDLog;
import com.github.DenFade.gdlistener.gd.log.GDLogProvider;
import com.github.DenFade.gdlistener.utils.FileStream;
import com.github.DenFade.gdlistener.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AwardedLevelUpdatedEvent extends AbstractEvent<GDLevel> {
    public AwardedLevelUpdatedEvent(){
        super(
                new AwardedLevelAddEventWorker(),
                new ListUpdatedScanner(),
                "awardedList.json"
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void dbUpdateAndNotify(List<GDLevel> newData, Context context, NotificationManager manager) {
        Gson gson = new Gson();
        try{
            JsonElement db = dbLoad();

            Type logType = new TypeToken<List<GDLog<GDLevel>>>(){}.getType();
            Type aliveType = new TypeToken<List<Long>>(){}.getType();

            List<Long> alive = gson.fromJson(db.getAsJsonObject().get("alive"), aliveType);
            List<GDLog<GDLevel>> log = gson.fromJson(db.getAsJsonObject().get("log"), logType);

            for(GDLevel l : newData){
                int type;
                if(l.getStars() == 0 || l.getName().equals("-")){
                    type = -1;
                    alive.remove(l.getId());
                }
                else {
                    type = 1;
                    alive.add(l.getId());
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channelId));
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), Utils.rscSelector(l)))
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(type == 1 ? context.getString(R.string.level_rate) : context.getString(R.string.level_unrate))
                        .setContentText(l.getName() + " by " + l.getCreatorName())
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
                log.add(new GDLog<GDLevel>(l.getId(), l, type));

                manager.notify((int) l.getId(), builder.build());
            }

            GDLog<GDLevel>[] log1 = log.toArray(new GDLog[log.size()]);
            Long[] alive1 = alive.toArray(new Long[alive.size()]);
            String provider = gson.toJson(new GDLogProvider<GDLevel>(log1, alive1));

            FileStream.write(dbPath, provider);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
