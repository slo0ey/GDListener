package com.github.DenFade.gdlistener.gd.entity;

public abstract class GDEntity {

    long id;

    public GDEntity(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }
}
