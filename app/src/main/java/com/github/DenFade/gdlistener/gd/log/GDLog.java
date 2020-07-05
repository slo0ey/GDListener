package com.github.DenFade.gdlistener.gd.log;

import com.github.DenFade.gdlistener.gd.entity.GDEntity;
import com.github.DenFade.gdlistener.gd.entity.GDLevel;

public class GDLog<E extends GDEntity>{

    private long id;
    private E entity;
    private int type;

    public GDLog(long id, E entity, int type) {
        this.id = id;
        this.entity = entity;
        this.type = type;
    }

    public E getEntity() {
        return entity;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "GDLogFormat[id=" + id +", entity=" + entity + ", type=" + type +"]";
    }
}
