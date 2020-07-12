package com.github.DenFade.gdlistener.event;

import android.app.Notification;
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
    private Notification notificationGroup = null;
    @Override
    @SuppressWarnings("unchecked")
    public void dbUpdateAndNotify(List<GDLevel> newData, Context context) {
        String groupKey = context.getString(R.string.awarded_event_group);
        if(notificationGroup==null){
            notificationGroup = new NotificationCompat.Builder(context, context.getString(R.string.awarded_event_channel_id))
                    .setContentTitle("")
                    //set content text to support devices running API level < 24
                    .setSmallIcon(R.drawable.icon)
                    //build summary info into InboxStyle template
                    .setStyle(new NotificationCompat.InboxStyle()
                            .setSummaryText("New Events"))
                    //specify which group this notification belongs to
                    .setGroup(groupKey)
                    //set this notification as the summary for the group
                    .setGroupSummary(true)
                    .build();
        }

        NotificationManager manager = context.getSystemService(NotificationManager.class);
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
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.awarded_event_channel_id));
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), Utils.rscSelector(l)))
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(type == 1 ? context.getString(R.string.level_rate) : context.getString(R.string.level_unrate))
                        .setContentText(l.getName() + " by " + l.getCreatorName())
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setGroup(groupKey);
                log.add(new GDLog<GDLevel>(l.getId(), l, type));

                manager.notify((int) l.getId(), builder.build());
                manager.notify(groupKey.hashCode(), notificationGroup);
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
