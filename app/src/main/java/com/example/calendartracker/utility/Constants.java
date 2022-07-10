package com.example.calendartracker.utility;

import android.Manifest;

public class Constants {
    public static final String BASE_URL = "https://calendar-tracker.bitjam.in";
    public static final int CALENDAR_PERMISSION_REQUEST_CODE = 379;

    public static final String[] PERMISSIONS = {
        Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};

    public static final String EVENT_ID = "EVENT_ID";
    public static final String RECORD_ID = "RECORD_ID";
}
