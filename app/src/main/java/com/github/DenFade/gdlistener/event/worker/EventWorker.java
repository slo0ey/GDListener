package com.github.DenFade.gdlistener.event.worker;

import android.content.Context;

import com.github.DenFade.gdlistener.gd.entity.GDEntity;

import java.util.List;

import okhttp3.OkHttpClient;

public interface EventWorker<E extends GDEntity> {

    List<E> work(Context context);

}
