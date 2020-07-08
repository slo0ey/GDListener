package com.github.DenFade.gdlistener.event.scanner;

import android.util.Log;

import com.github.DenFade.gdlistener.gd.GDLevelSearchRequest;
import com.github.DenFade.gdlistener.gd.entity.GDLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListUpdatedScanner implements EventScanner<GDLevel> {

    @Override
    public List<GDLevel> scan(List<Long> preData, List<GDLevel> newData) {
        List<GDLevel> updated = new ArrayList<>();
        List<GDLevel> added = newData.stream()
                .filter(l -> !preData.contains(l.getId()))
                .collect(Collectors.toList());
        /*List<GDLevel> removed = (preData.size() < 10 ? preData : preData.subList(0, 10)).stream()
                .filter(l -> newData.stream().noneMatch(l2 -> l2.getId() == l))
                .map(l -> {
                        try {
                            return new GDLevelSearchRequest(15000, "" + l, GDUtils.LevelFilter.DEFAULT, GDUtils.LevelFilter.create(), 0)
                                .fetch().get(0);
                        } catch (IllegalAccessException e){
                            Log.d("Level Removed!", Objects.requireNonNull(e.getMessage()));
                            return GDLevel.empty(l);
                        }
                    }
                )
                .collect(Collectors.toList());*/
        updated.addAll(added);
        System.out.println(newData);
        System.out.println(updated);
        //updated.addAll(removed);
        return updated;
    }
}
