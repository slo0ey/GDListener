package com.github.DenFade.gdlistener.event;

import android.content.Context;

import com.github.DenFade.gdlistener.event.scanner.UpdateScanner;
import com.github.DenFade.gdlistener.event.worker.EventWorker;
import com.github.DenFade.gdlistener.gd.entity.GDLevel;

import java.util.List;

public class TimelyLevelUpdateEvent extends AbstractEvent<GDLevel> {
    public TimelyLevelUpdateEvent(EventWorker<GDLevel> worker, UpdateScanner<GDLevel> scanner, String dbPath) {
        super(worker, scanner, dbPath);
    }

    @Override
    public void dbUpdateAndNotify(List<GDLevel> newData, Context context) {

    }
}
