package com.attinad.analyticsengine.core.datamodels.core;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by unnikrishanansr on 14/8/17.
 */

public class BaseEventMap implements Map<String, Object> {

    private Map<String, Object> properties = new HashMap<>();

    public BaseEventMap() {

    }

    public BaseEventMap(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null.");
        }
        this.properties = map;
    }

    public BaseEventMap putValue(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    @Override
    public int size() {
        return properties.size();
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return properties.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return properties.containsValue(o);
    }

    @Override
    public Object get(Object o) {
        return properties.get(o);
    }

    @Override
    public Object put(String s, Object o) {
        return properties.put(s, o);
    }

    @Override
    public Object remove(Object o) {
        return properties.remove(o);
    }

    @Override
    public void putAll(@NonNull Map<? extends String, ?> map) {
        properties.putAll(map);
    }

    @Override
    public void clear() {
        properties.clear();
    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return properties.keySet();
    }

    @NonNull
    @Override
    public Collection<Object> values() {
        return properties.values();
    }

    @NonNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return properties.entrySet();
    }


    public Map<String, Object> getProperties() {
        return properties;
    }

    public HashMap<String, Object> getPropertyMap() {

        HashMap<String, Object> hMap = new HashMap<>();
        if (properties != null && properties instanceof HashMap<?, ?>) {
            hMap = (HashMap<String, Object>) properties;
        } else if (properties != null) {
            hMap.putAll(properties);
        }

        return hMap;
    }
}
