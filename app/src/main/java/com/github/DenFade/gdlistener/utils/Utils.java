package com.github.DenFade.gdlistener.utils;

import com.github.DenFade.gdlistener.R;
import com.github.DenFade.gdlistener.gd.entity.GDLevel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;

public class Utils {

    private Utils(){

    }

    public static String defaultOrGet(String str, String replacement){
        return str == null ? Objects.requireNonNull(replacement) : str;
    }

    private static int rscSubselector(GDLevel level, int r1, int r2, int r3){
        if(level.isEpic()) return r1;
        else if(level.getFeaturedScore() != 0) return r2;
        else return r3;
    }

    public static int rscSelector(GDLevel level){
        if(level.isAuto()){
            return rscSubselector(level, R.drawable.auto_e, R.drawable.auto_f, R.drawable.auto);
        } else if(level.isDemon()){
            switch(level.getDemonDifficulty()){
                case 3:
                    return rscSubselector(level, R.drawable.easy_demon_e, R.drawable.easy_demon_f, R.drawable.easy_demon);
                case 4:
                    return rscSubselector(level, R.drawable.medium_demon_e, R.drawable.medium_demon_f, R.drawable.medium_demon);
                case 5:
                    return rscSubselector(level, R.drawable.insane_demon_e, R.drawable.insane_demon_f, R.drawable.insane_demon);
                case 6:
                    return rscSubselector(level, R.drawable.extreme_demon_e, R.drawable.extreme_demon_f, R.drawable.extreme_demon);
                default:
                    return rscSubselector(level, R.drawable.hard_demon_e, R.drawable.hard_demon_f, R.drawable.hard_demon);
            }
        } else {
            switch (level.getDifficulty()){
                case 10:
                    return rscSubselector(level, R.drawable.easy_e, R.drawable.easy_f, R.drawable.easy);
                case 20:
                    return rscSubselector(level, R.drawable.normal_e, R.drawable.normal_f, R.drawable.normal);
                case 30:
                    return rscSubselector(level, R.drawable.hard_e, R.drawable.hard_f, R.drawable.hard);
                case 40:
                    return rscSubselector(level, R.drawable.harder_e, R.drawable.harder_f, R.drawable.harder);
                case 50:
                    return rscSubselector(level, R.drawable.insane_e, R.drawable.insane_f, R.drawable.insane);
                default:
                    return rscSubselector(level, R.drawable.na_e, R.drawable.na_f, R.drawable.na);
            }
        }
    }

    public static String encodeURI(String str){
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String decodeURI(String uri) {
        try {
            return URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return uri;
        }
    }
}
