package com.github.DenFade.gdlistener.event.worker;

import com.github.DenFade.gdlistener.gd.GDLevelSearchRequest;
import com.github.DenFade.gdlistener.gd.entity.GDLevel;
import com.github.DenFade.gdlistener.utils.GDUtils;

import java.util.List;

import okhttp3.OkHttpClient;

public class AwardedLevelAddEventWorker implements EventWorker<GDLevel> {

    @Override
    public List<GDLevel> work() {
        try {
            return new GDLevelSearchRequest(15000, "", GDUtils.LevelFilter.AWARDED, GDUtils.LevelFilter.create(), 0)
                    .fetch();
        } catch(IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
    }
}
