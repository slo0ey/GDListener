package com.github.DenFade.gdlistener.event;

import android.util.Log;

import com.github.DenFade.gdlistener.event.scanner.EventScanner;
import com.github.DenFade.gdlistener.event.worker.EventWorker;
import com.github.DenFade.gdlistener.gd.entity.GDEntity;
import com.github.DenFade.gdlistener.gd.log.GDLogProvider;
import com.github.DenFade.gdlistener.utils.FileStream;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public abstract class AbstractEvent<E extends GDEntity> {
    final EventWorker<E> worker;
    final EventScanner<E> scanner;
    final String dbPath;

    public AbstractEvent(EventWorker<E> worker, EventScanner<E> scanner, String dbPath){
        this.worker = worker;
        this.scanner = scanner;
        this.dbPath = FileStream.ROOT_DIR + dbPath;
    }

    public void dbInit(){
        File db = new File(dbPath);
        if(!db.exists()){
            Log.d("DB", "Path: " + dbPath + "\nExist: false");
            try{
                Log.d("DB", "작성 전");
                FileStream.write(dbPath, "{ \"alive\":[],\"log\":[]}");
                Log.d("DB", "작성후: path - " + dbPath + "\n"+ FileStream.read(dbPath));
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            Log.d("DB", "Path: " + dbPath + "\nExist: true");
        }
    }

    public JsonElement dbLoad(){
        try {
            return JsonParser.parseString(FileStream.read(dbPath));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void dbUpdate(List<E> newData){

    }

    public List<E> run(){
        return worker.work();
    }

    public List<E> filter(List<Long> preList, List<E> newList){
        return scanner.scan(preList, newList);
    }
}
