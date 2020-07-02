package com.github.DenFade.gdlistener.event.scanner;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDLevel;
import com.github.alex1304.jdash.util.LevelSearchFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListUpdatedScanner implements EventScanner<GDLevel> {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<GDLevel> scan(List<Long> preData, List<GDLevel> newData) {
        List<GDLevel> updated = new ArrayList<>();
        List<GDLevel> added = newData.stream()
                .filter(l -> !preData.contains(l.getId()))
                .collect(Collectors.toList());
        List<GDLevel> removed = preData.stream()
                .filter(l -> newData.stream().noneMatch(l2 -> l2.getId() == l))
                .map(l -> Optional.ofNullable(GDClientBuilder.create().buildAnonymous()
                        .searchLevels(l + "", LevelSearchFilters.create(), 0).block()
                        .asList()).orElse((List<GDLevel>) new ArrayList<GDLevel>()).get(0)
                )
                .collect(Collectors.toList());
        updated.addAll(added);
        updated.addAll(removed);
        return updated;
    }
}
