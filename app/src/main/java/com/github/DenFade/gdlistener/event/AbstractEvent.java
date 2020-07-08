package com.github.DenFade.gdlistener.event;

import android.app.NotificationManager;
import android.content.Context;

import com.github.DenFade.gdlistener.event.scanner.EventScanner;
import com.github.DenFade.gdlistener.event.worker.EventWorker;
import com.github.DenFade.gdlistener.gd.entity.GDEntity;
import com.github.DenFade.gdlistener.utils.FileStream;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
        this.dbPath = FileStream.ROOT_DIR + dbPath;
    }

    public void dbInit(){
        File db = new File(dbPath);
        if(!db.exists()){
            try{
                FileStream.write(dbPath, "{ \"alive\":[],\"log\":[]}");
            } catch (IOException e){
                e.printStackTrace();
            }
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

    public void dbUpdateAndNotify(List<E> newData, Context context, NotificationManager manager){

    }

    public List<E> run(){
        return worker.work();
    }

    public List<E> filter(List<Long> preList, List<E> newList){
        return scanner.scan(preList, newList);
    }
}
