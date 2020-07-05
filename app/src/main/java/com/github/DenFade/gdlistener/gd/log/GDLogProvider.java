package com.github.DenFade.gdlistener.gd.log;

import com.github.DenFade.gdlistener.gd.entity.GDEntity;

import java.util.List;

public class GDLogProvider<E extends GDEntity> {
    private GDLog<E>[] log;
    private Long[] alive;

    public GDLogProvider(GDLog<E>[] log, Long[] alive){
        this.log = log;
        this.alive = alive;
    }
}
