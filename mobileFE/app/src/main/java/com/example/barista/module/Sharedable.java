package com.example.barista.module;

import java.util.HashMap;
import java.util.Map;

public class Sharedable {
    private static final Map<String, Object> sharedData = new HashMap<>();

    public static void put(String key, Object value) {
        synchronized (sharedData) {
            sharedData.put(key, value);
        }
    }

    public static Object get(String key) {
        synchronized (sharedData) {
            return sharedData.get(key);
        }
    }

    public static void remove(String key) {
        synchronized (sharedData) {
            sharedData.remove(key);
        }
    }
}
