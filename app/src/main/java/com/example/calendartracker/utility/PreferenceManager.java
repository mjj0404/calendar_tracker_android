package com.example.calendartracker.utility;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PreferenceManager {
    private static final String PREF_NAME = "com.example.app.PREF_NAME";
    private static final String KEY_VALUE = "com.example.app.KEY_VALUE";

    private static PreferenceManager instance;
    private final SharedPreferences pref;

    private PreferenceManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }
    }

    public static synchronized PreferenceManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(PreferenceManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public Map<String, Long> loadEventHashMap() {
        Map<String, Long> outputMap = new HashMap<>();
        try {
            if (pref != null) {
                String jsonString = pref.getString(Constants.ID_MAP_RECORD_EVENT, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);

                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    long value = jsonObject.getLong(key);
                    outputMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

    private void storeEventHashMap(Map<String, Long> inputMap) {
        if (pref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(Constants.ID_MAP_RECORD_EVENT).apply();
            editor.putString(Constants.ID_MAP_RECORD_EVENT, jsonString);
            editor.apply();
        }
    }

    public void setValue(long value) {
        pref.edit()
                .putLong(KEY_VALUE, value)
                .commit();
    }

    public long getValue() {
        return pref.getLong(KEY_VALUE, 0);
    }

    public void remove(String key) {
        pref.edit()
                .remove(key)
                .commit();
    }

    public boolean clear() {
        return pref.edit()
                .clear()
                .commit();
    }
}
