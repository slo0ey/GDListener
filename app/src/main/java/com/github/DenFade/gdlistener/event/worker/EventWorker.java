package com.github.DenFade.gdlistener.event.worker;

import com.github.alex1304.jdash.client.AuthenticatedGDClient;
import com.github.alex1304.jdash.entity.GDEntity;
import com.github.alex1304.jdash.util.GDPaginator;

public interface EventWorker<E extends GDEntity> {

    GDPaginator<E> work(AuthenticatedGDClient client);

}
