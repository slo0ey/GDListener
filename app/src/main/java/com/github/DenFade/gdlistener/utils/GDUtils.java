package com.github.DenFade.gdlistener.utils;

import com.github.DenFade.gdlistener.gd.entity.GDSong;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GDUtils {
    private GDUtils(){

    }

    public static HashMap<String, String> parseToMap(String str, String regex){
        String[] splitted = str.split(regex);
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < splitted.length-1; i+=2) {
            map.put(splitted[i], splitted[i+1]);
        }
        return map;
    }

    public static Map<Long, String> creatorInfoExtractor(String str){
        if(str.isEmpty()) return Collections.emptyMap();
        String[] splitted = str.split("\\|");
        Map<Long, String> creatorList = new HashMap<>();
        for(String data : splitted){
            String[] datas = data.split(":");
            creatorList.put(Long.parseLong(datas[0]), datas[1]);
        }
        return creatorList;
    }

    public static Map<Long, GDSong> songInfoExtractor(String str){
        if(str.isEmpty()) return Collections.emptyMap();
        String[] splitted = str.split("~:~");
        Map<Long, GDSong> songList = new HashMap<>();
        for(String data : splitted){
            Map<String, String> songData = parseToMap(data, "~\\|~");
            long songId = Long.parseLong(Utils.defaultOrGet(songData.get("1"), "0"));
            songList.put(songId,
                    new GDSong(songId,
                            Utils.defaultOrGet(songData.get("2"), "-"),
                            Utils.defaultOrGet(songData.get("4"), "Unknown"),
                            Utils.defaultOrGet(songData.get("5"), "??MB"),
                            Utils.decodeURI(Utils.defaultOrGet(songData.get("10"), "")),
                            true
                    ));
        }
        return songList;
    }

    public static class LevelFilter {

        public static final int DEFAULT = 0;
        public static final int MOST_DOWNLOADED = 1;
        public static final int MOST_LIKED = 2;
        public static final int TRENDING = 3;
        public static final int RECENT = 4;
        public static final int BY_USER = 5;
        public static final int FEATURED = 6;
        public static final int MAGIC = 7;
        public static final int AWARDED = 11;
        public static final int FOLLOWED = 12;
        public static final int HALL_OF_FAME = 16;

        public static final int TINY = 0;
        public static final int SHORT = 1;
        public static final int MEDIUM = 2;
        public static final int LONG = 3;
        public static final int XL = 4;

        private HashMap<String, Integer> table;

        private LevelFilter(){
            this.table = new HashMap<>();

            table.put("uncompleted", 0);
            table.put("onlyCompleted", 0);
            table.put("featured", 0);
            table.put("original", 0);
            table.put("twoPlayer", 0);
            table.put("coins", 0);
            table.put("epic", 0);
            table.put("star", 0);
        }

        public static LevelFilter create(){
            return new LevelFilter();
        }

        public LevelFilter toggle(String key, Integer value){
            if(table.containsKey(key)) table.replace(key, value);
            return this;
        }

        public LevelFilter setLength(int l){
            if(TINY <= l && l <= XL) table.put("len", l);
            return this;
        }

        public HashMap<String, Integer> getTable(){
            return table;
        }
    }
}
