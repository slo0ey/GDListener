package com.github.DenFade.gdlistener.event;

import com.github.DenFade.gdlistener.event.scanner.EventScanner;
import com.github.DenFade.gdlistener.event.worker.EventWorker;
import com.github.DenFade.gdlistener.utils.FileUtils;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.entity.GDEntity;
import com.github.alex1304.jdash.util.GDPaginator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class AbstractEvent<E extends GDEntity> {
    final EventWorker<E> worker;
    final EventScanner<E> scanner;
    final String dbPath;

    public AbstractEvent(EventWorker<E> worker, EventScanner<E> scanner, String dbPath){
        this.worker = worker;
        this.scanner = scanner;
        this.dbPath = FileUtils.ROOT_DIR + "db\\/" + dbPath;
    }

    public AbstractEvent<E> dbInit(){
        File db = new File(dbPath);
        if(!db.exists()){
            try{
                FileUtils.writeFile(dbPath, new JSONObject("{ \"alive\":[],\"log\":[]}").toString(4));
            } catch (IOException | JSONException e){
                e.printStackTrace();
            }
        }
        return this;
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

    public GDPaginator<E> run(AnonymousGDClient client){
        return worker.work(client);
    }

    public List<E> filter(List<Long> preList, List<E> newList){
        return scanner.scan(preList, newList);
    }
}
