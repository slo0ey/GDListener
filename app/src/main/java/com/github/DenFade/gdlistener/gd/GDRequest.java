package com.github.DenFade.gdlistener.gd;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

abstract class GDRequest<E> {
    private OkHttpClient client;
    protected HashMap<String, String> param = new HashMap<>();

    private static final MediaType contentType = MediaType.get("application/x-www-form-urlencoded");
    public static final String BaseURL = "https://www.boomlings.com/database/";

    public GDRequest(int timeout){
        this.client = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();
    }

    public String getUrl(){
        return "";
    }

    public void putAdditionalParams(){

    }

    public E fetch() throws IllegalAccessException{
        StringJoiner sj = new StringJoiner("&");
        param.put("gameVersion", "21");
        param.put("binaryVersion", "35");
        param.put("gdw", "0");
        param.put("secret", "Wmfd2893gb7");
        putAdditionalParams();
        param.forEach((k, v) -> sj.add(k+"="+v));

        String result;
        RequestBody body = RequestBody.create(sj.toString(), contentType);
        Request request = new Request.Builder()
                .url(getUrl())
                .post(body)
                .build();
        try {
            result = Objects.requireNonNull(client.newCall(request)
                    .execute()
                        .body()).string();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            result = "-1";
        }
        if(result == "-1") throw new IllegalAccessException("No such result! <Response code: -1>");
        return parseResponse(result);
    }

    E parseResponse(String res) {
        return null;
    }
}
