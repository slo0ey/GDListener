package com.github.DenFade.gdlistener.event;

import com.github.DenFade.gdlistener.event.scanner.EventScanner;
import com.github.DenFade.gdlistener.event.worker.EventWorker;
import com.github.DenFade.gdlistener.utils.FileUtils;
import com.github.alex1304.jdash.entity.GDEntity;

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
                FileWriter fr = new FileWriter(dbPath);
                fr.write("");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return this;
    }

    public void dbUpdate(List<E> newData){

    }

    public void run(){

    }
}
