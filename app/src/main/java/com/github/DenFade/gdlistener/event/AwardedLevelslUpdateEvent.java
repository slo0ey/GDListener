package com.github.DenFade.gdlistener.event;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.github.DenFade.gdlistener.R;
import com.github.DenFade.gdlistener.event.scanner.DefaultLevelsUpdateScanner;
import com.github.DenFade.gdlistener.event.worker.AwardedLevelsUpdateEventWorker;
import com.github.DenFade.gdlistener.gd.entity.GDLevel;
import com.github.DenFade.gdlistener.utils.FileStream;
import com.github.DenFade.gdlistener.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AwardedLevelslUpdateEvent extends AbstractEvent<GDLevel> {
    public AwardedLevelslUpdateEvent(){
        super(
                new AwardedLevelsUpdateEventWorker(),
                new DefaultLevelsUpdateScanner(),
                "awardedList.txt"
        );
    }

    private Notification notificationGroup = null;

    @Override
    public void dbUpdateAndNotify(List<GDLevel> newData, Context context) {
        String groupKey = context.getString(R.string.awarded_event_group);
        if(notificationGroup==null){
            notificationGroup = new NotificationCompat.Builder(context, context.getString(R.string.awarded_event_channel_id))
                    .setContentTitle("")
                    //set content text to support devices running API level < 24
                    .setSmallIcon(R.drawable.icon)
                    //build summary info into InboxStyle template
                    .setStyle(new NotificationCompat.InboxStyle()
                            .setSummaryText("* Awarded *"))
                    //specify which group this notification belongs to
                    .setGroup(groupKey)
                    //set this notification as the summary for the group
                    .setGroupSummary(true)
                    .build();
        }

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        Gson gson = new Gson();
        try{
            ArrayList<Long> alive = new ArrayList<>(dbLoad());
            System.out.println(dbPath);

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

                manager.notify((int) l.getId(), builder.build());
                manager.notify(groupKey.hashCode(), notificationGroup);
            }
            FileStream.write(dbPath, alive.stream().map(String::valueOf).collect(Collectors.joining(",")));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
