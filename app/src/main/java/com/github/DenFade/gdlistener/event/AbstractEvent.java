package com.github.DenFade.gdlistener.event;

import android.util.Log;

import com.github.DenFade.gdlistener.event.scanner.EventScanner;
import com.github.DenFade.gdlistener.event.worker.EventWorker;
import com.github.DenFade.gdlistener.gd.entity.GDEntity;
import com.github.DenFade.gdlistener.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class AbstractEvent<E extends GDEntity> {
    final EventWorker<E> worker;
    final EventScanner<E> scanner;
    final String dbPath;

    public AbstractEvent(EventWorker<E> worker, EventScanner<E> scanner, String dbPath){
        this.worker = worker;
        this.scanner = scanner;
        this.dbPath = FileUtils.ROOT_DIR + dbPath;
    }

    public void dbInit(){
        File db = new File(dbPath);
        if(!db.exists()){
            Log.d("DB", "Path: " + dbPath + "\nExist: false");
            try{
                Log.d("DB", "작성 전");
                FileUtils.writeFile(dbPath, new JSONObject("{ \"alive\":[],\"log\":[]}").toString(4));
                Log.d("DB", "작성후: path - " + dbPath + "\n"+FileUtils.readFile(dbPath));
            } catch (IOException | JSONException e){
                e.printStackTrace();
            }
        } else {
            Log.d("DB", "Path: " + dbPath + "\nExist: true");
        }
    }

    public JSONObject dbLoad(){
        try{
            return new JSONObject(FileUtils.readFile(dbPath));
        } catch (IOException | JSONException e){
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
