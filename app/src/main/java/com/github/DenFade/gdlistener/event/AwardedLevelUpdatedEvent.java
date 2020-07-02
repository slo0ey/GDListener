package com.github.DenFade.gdlistener.event;

import com.github.DenFade.gdlistener.event.scanner.ListUpdatedScanner;
import com.github.DenFade.gdlistener.event.worker.AwardedLevelAddEventWorker;
import com.github.DenFade.gdlistener.utils.FileUtils;
import com.github.alex1304.jdash.entity.GDLevel;

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
                "awardedList.txt"
        );
    }

    @Override
    public void dbUpdate(List<GDLevel> newData) {
        try{
            JSONObject db = new JSONObject(FileUtils.readFile(dbPath));
            JSONArray aliveList = db.getJSONArray("alive");
            JSONArray log = db.getJSONArray("log");
            for(GDLevel l : newData){
                JSONObject table = new JSONObject();
                if(l.getStars() == 0){
                    table.put("type", -1);
                } else {
                    table.put("type", 1);
                }
            }

            FileUtils.writeFile(dbPath, db.toString(4));
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }
}
