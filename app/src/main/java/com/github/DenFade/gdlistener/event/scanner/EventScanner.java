package com.github.DenFade.gdlistener.event.scanner;

import com.github.DenFade.gdlistener.gd.entity.GDEntity;

import java.util.List;

public interface EventScanner<E extends GDEntity> {

    /**
     * Compare the old data list with the new data list and extract the required data list
     *
     * @param preData List of pre data
     * @param newData List of new data
     * @return List of extracted data
     */

    List<E> scan(List<Long> preData, List<E> newData);

}
