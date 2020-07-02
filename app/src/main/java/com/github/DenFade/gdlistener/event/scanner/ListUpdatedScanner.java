package com.github.DenFade.gdlistener.event.scanner;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.alex1304.jdash.entity.GDLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListUpdatedScanner implements EventScanner<GDLevel> {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<GDLevel> scan(List<GDLevel> preData, List<GDLevel> newData) {
        List<GDLevel> updated = new ArrayList<>();
        List<GDLevel> added = newData.stream()
                .filter(l -> !preData.contains(l))
                .collect(Collectors.toList());
        List<GDLevel> removed = preData.stream()
                .filter(l -> !newData.contains(l))
                .map(l -> l.refresh().block())
                .collect(Collectors.toList());
        updated.addAll(added);
        updated.addAll(removed);
        return updated;
    }
}
