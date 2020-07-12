package com.github.DenFade.gdlistener.event.worker;

import android.content.Context;

import com.github.DenFade.gdlistener.gd.GDLevelSearchRequest;
import com.github.DenFade.gdlistener.gd.entity.GDLevel;
import com.github.DenFade.gdlistener.utils.GDUtils;

import java.util.List;

public class AwardedLevelsUpdateEventWorker implements EventWorker<GDLevel> {

    @Override
    public List<GDLevel> work(Context context) {
        try {
            return new GDLevelSearchRequest(8000, "", GDUtils.LevelFilter.AWARDED, GDUtils.LevelFilter.create(), 0)
                    .fetch();
        } catch(IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
    }
}
