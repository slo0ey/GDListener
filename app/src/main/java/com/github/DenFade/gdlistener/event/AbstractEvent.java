package com.github.DenFade.gdlistener.event;

import android.content.Context;

import com.github.DenFade.gdlistener.event.scanner.UpdateScanner;
import com.github.DenFade.gdlistener.event.worker.EventWorker;
import com.github.DenFade.gdlistener.gd.entity.GDEntity;
import com.github.DenFade.gdlistener.utils.FileStream;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractEvent<E extends GDEntity> {
    final EventWorker<E> worker;
    final UpdateScanner<E> scanner;
    final String dbPath;

    public AbstractEvent(EventWorker<E> worker, UpdateScanner<E> scanner, String dbPath){
        this.worker = worker;
        this.scanner = scanner;
        this.dbPath = FileStream.ROOT_DIR + dbPath;
    }

    public void dbInit(){
        File db = new File(dbPath);
        if(!db.exists()){
            try{
                FileStream.write(dbPath, "");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public List<Long> dbLoad(){
        String db = FileStream.read(dbPath, "");
        return db.equals("") ? Collections.emptyList() : Arrays.stream(db.split(",")).map(Long::parseLong).collect(Collectors.toList());
    }

    public abstract void dbUpdateAndNotify(List<E> newData, Context context);

    public List<E> run(Context context){
        return worker.work(context);
    }

    public List<E> filter(List<Long> preList, List<E> newList){
        return scanner.scan(preList, newList);
    }
}
