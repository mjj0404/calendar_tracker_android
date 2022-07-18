package com.example.calendartracker;

import android.app.Application;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.calendartracker.utility.PreferenceManager;

public class CalendarTrackerApplication extends Application {

    private static final String TAG = "CalendarTrackerApplication";



    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.init(this);
    }

    public static void setApplicationTheme (int themeSelection) {
        PreferenceManager.getInstance().setTheme(themeSelection);
        AppCompatDelegate.setDefaultNightMode(themeSelection);
    }
}
