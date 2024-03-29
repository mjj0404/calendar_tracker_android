package com.bitjam.calendartracker.utility;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PreferenceManager {
    private static final String PREF_NAME = "com.example.app.PREF_NAME";

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

    public Map<String, String> loadEventHashMap() {
        Map<String, String> outputMap = new HashMap<>();
        try {
            if (pref != null) {
                String jsonString = pref.getString(Constants.ID_MAP_RECORD_EVENT, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);

                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = jsonObject.getString(key);
                    outputMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

    public void storeEventHashMap(Map<String, String> inputMap) {
        if (pref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(Constants.ID_MAP_RECORD_EVENT).apply();
            editor.putString(Constants.ID_MAP_RECORD_EVENT, jsonString);
            editor.apply();
        }
    }
    public void setFirstTime (boolean param) {
        pref.edit().putBoolean(Constants.FIRST_TIME, param).apply();
    }

    public boolean isFirstTime() {
        return pref.getBoolean(Constants.FIRST_TIME, true);
    }


    public void setSingleItemParsingAllowed (boolean param) {
        pref.edit().putBoolean(Constants.DIALOG_DO_NOT_ASK, param).apply();
    }

    public boolean isSingleItemParsingAllowed() {
        return pref.getBoolean(Constants.DIALOG_DO_NOT_ASK, false);
    }

    public void setThemeChanged (boolean param) {
        pref.edit().putBoolean(Constants.THEME_CONFIGURATION_CHANGED, param).apply();
    }

    public boolean isThemeChanged() {
        if (pref.getBoolean(Constants.THEME_CONFIGURATION_CHANGED, false)) {
            pref.edit().putBoolean(Constants.THEME_CONFIGURATION_CHANGED, false).apply();
            return true;
        }
        return false;
    }

    public void setReminderSetting(int param) {
        pref.edit().putInt(Constants.BEFORE_EVENT, param).apply();
    }

    public int getReminderSetting() {
        return pref.getInt(Constants.BEFORE_EVENT, 0);
    }

    public void addReminder(boolean param) {
        pref.edit().putBoolean(Constants.ADD_REMINDER_SELECTION, param).apply();
    }

    public boolean isAddingReminder() {
        return pref.getBoolean(Constants.ADD_REMINDER_SELECTION, false);
    }

    public void setTheme(int param) {
        pref.edit().putInt(Constants.THEME_SELECTION, param).apply();
    }

    public int getTheme() {
        return pref.getInt(Constants.THEME_SELECTION, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
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
