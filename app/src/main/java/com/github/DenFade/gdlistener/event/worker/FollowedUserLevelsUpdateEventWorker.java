package com.github.DenFade.gdlistener.event.worker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.github.DenFade.gdlistener.gd.GDLevelSearchRequest;
import com.github.DenFade.gdlistener.gd.entity.GDLevel;
import com.github.DenFade.gdlistener.utils.GDUtils;
import com.github.DenFade.gdlistener.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class FollowedUserLevelsUpdateEventWorker implements EventWorker<GDLevel> {

    @Override
    public List<GDLevel> work(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String followedList = preferences.getString("followedList", "");
            if(!Utils.isVaildListForm(followedList)) return null;
            return new GDLevelSearchRequest(8000, "", GDUtils.LevelFilter.FOLLOWED, GDUtils.LevelFilter.create(), 0, Arrays.asList(followedList.split(",")))
                    .fetch();
        } catch(IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
    }
}
