package com.github.DenFade.gdlistener.event.worker;

import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.entity.GDLevel;
import com.github.alex1304.jdash.util.GDPaginator;
import com.github.alex1304.jdash.util.LevelSearchFilters;

public class AwardedLevelAddEventWorker implements EventWorker<GDLevel> {

    @Override
    public GDPaginator<GDLevel> work(AnonymousGDClient client) {
        return client.browseAwardedLevels(LevelSearchFilters.create(), 0)
                .doOnError(Throwable::printStackTrace)
                .block();
    }
}
