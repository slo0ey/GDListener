package com.github.DenFade.gdlistener.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Objects;

public class Utils {

    private Utils(){

    }

    public static String defaultOrGet(String str, String replacement){
        return str == null ? Objects.requireNonNull(replacement) : str;
    }

    public static String imageNameGetter(HashMap<String, String> level){
        return "";
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
