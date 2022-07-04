package com.example.calendartracker.utility;

import android.content.Context;
import android.content.SharedPreferences;

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
