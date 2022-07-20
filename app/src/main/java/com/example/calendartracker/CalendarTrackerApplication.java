package com.example.calendartracker;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.calendartracker.utility.PreferenceManager;

public class CalendarTrackerApplication extends Application {

    private static final String TAG = "CalendarTrackerApplication";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        PreferenceManager.init(this);
    }

    public static Context getContext() {
        return context;
    }

    public static void setApplicationTheme (int themeSelection) {
        PreferenceManager.getInstance().setTheme(themeSelection);
        AppCompatDelegate.setDefaultNightMode(themeSelection);
    }
}
