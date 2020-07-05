package com.github.DenFade.gdlistener.gd;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.github.DenFade.gdlistener.gd.entity.GDLevel;
import com.github.DenFade.gdlistener.gd.entity.GDSong;
import com.github.DenFade.gdlistener.utils.GDUtils;
import com.github.DenFade.gdlistener.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;

public class GDLevelSearchRequest extends GDRequest<List<GDLevel>>{

    private String query;
    private int strategy;
    private GDUtils.LevelFilter filter;
    private int page;
    private Collection<? extends Long> followed;

    public GDLevelSearchRequest(int timeout, String query, int strategy, GDUtils.LevelFilter filter, int page) {
        super(timeout);
        this.query = query;
        this.strategy = strategy;
        this.filter = filter;
        this.page = page;
    }

    public GDLevelSearchRequest(int timeout, String query, int strategy, GDUtils.LevelFilter filter, int page, Collection<? extends Long> followed) {
        super(timeout);
        this.query = query;
        this.strategy = strategy;
        this.filter = filter;
        this.page = page;
        this.followed = followed;
    }

    @Override
    public String getUrl() {
        return BaseURL + "getGJLevels21.php";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void putAdditionalParams() {
        param.put("str", Utils.encodeURI(query));
        param.put("type", ""+strategy);
        param.put("page", ""+page);
        filter.getTable().forEach((k, v) -> param.put(k, ""+v));
        if(!param.containsKey("diff")) param.put("diff", "-");
        if(!param.containsKey("len")) param.put("len", "-");
        if(strategy == GDUtils.LevelFilter.FOLLOWED && !followed.isEmpty()){
            param.put("followed", String.join(",", followed.stream().map(String::valueOf).collect(Collectors.toSet())));
        }
    }

    @Override
    List<GDLevel> parseResponse(String res) {
        String[] resArray = res.split("#");
        String level = resArray[0];
        String creator = resArray[1];
        String song = resArray[2];
        String pageInfo = resArray[3]; //umm.. where we use?

        Map<Long, String> creators = GDUtils.creatorInfoExtractor(creator);
        Map<Long, GDSong> songs = GDUtils.songInfoExtractor(song);
        List<GDLevel> levels = new ArrayList<>();

        for(String lv : level.split("\\|")){
            Map<String, String> l = GDUtils.parseToMap(lv, ":");
            Long levelId = Long.parseLong(Utils.defaultOrGet(l.get("1"), "0"));
            Long songId = Long.parseLong(Utils.defaultOrGet(l.get("35"), "0"));
            String creatorName = Utils.defaultOrGet(
                                    creators.get(
                                            Long.parseLong(Utils.defaultOrGet(l.get("6"), "0"))
                                    ), "Unknown"
                                );
            GDSong song0 = songId <= 0 ?
                    GDSong.AUDIO.get(Integer.parseInt(Utils.defaultOrGet(l.get("12"), "0")))
                    : Optional.ofNullable(songs.get(songId)).orElse(GDSong.empty(songId));
            levels.add(
                    new GDLevel(
                            levelId,
                            Utils.defaultOrGet(l.get("2"), "-"),
                            creatorName,
                            Integer.parseInt(Utils.defaultOrGet(l.get("14"), "0")),
                            Integer.parseInt(Utils.defaultOrGet(l.get("10"), "0")),
                            Integer.parseInt(Utils.defaultOrGet(l.get("18"), "0")),
                            Integer.parseInt(Utils.defaultOrGet(l.get("9"), "0")),
                            Utils.defaultOrGet(l.get("17"), "0").equals("1"),
                            Utils.defaultOrGet(l.get("25"), "0").equals("1"),
                            Integer.parseInt(Utils.defaultOrGet(l.get("30"), "0")),
                            Utils.defaultOrGet(l.get("42"), "0").equals("1"),
                            Integer.parseInt(Utils.defaultOrGet(l.get("37"), "0")),
                            Utils.defaultOrGet(l.get("38"), "0").equals("1"),
                            Integer.parseInt(Utils.defaultOrGet(l.get("45"), "0")),
                            song0
                    )
            );
        }
        return levels;
    }
}
