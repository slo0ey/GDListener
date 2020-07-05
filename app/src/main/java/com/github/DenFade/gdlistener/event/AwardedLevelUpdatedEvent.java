package com.github.DenFade.gdlistener.event;

import com.github.DenFade.gdlistener.event.scanner.ListUpdatedScanner;
import com.github.DenFade.gdlistener.event.worker.AwardedLevelAddEventWorker;
import com.github.DenFade.gdlistener.gd.entity.GDLevel;
import com.github.DenFade.gdlistener.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    public void dbUpdate(List<GDLevel> newData) {
        try{
            JSONObject db = dbLoad();
            List<Long> aliveList = (List<Long>) db.getJSONArray("alive");
            JSONArray log = db.getJSONArray("log");
            for(GDLevel l : newData){
                JSONObject obj = new JSONObject();
                if(l == null || l.getStars() == 0){
                    if(l != null) aliveList.remove(l.getId());
                    obj.put("type", -1);
                } else {
                    aliveList.add(l.getId());
                    obj.put("type", 1);
                }
                obj.put("id", l.getId());
                obj.put("name", l.getName());
                obj.put("cName", l.getCreatorName());
                obj.put("like", l.getLikes());
                obj.put("dl", l.getDownloads());
                obj.put("copy", l.getOriginalId() == 0);
                obj.put("epic", l.isEpic());
                obj.put("coin", l.getCoins());
                obj.put("cv", l.hasVerifiedCoin());

            }

            FileUtils.writeFile(dbPath, db.toString(4));
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }
}
