package com.example.calendartracker.utility;

import android.Manifest;

public class Constants {
    public static final String BASE_URL = "https://calendar-tracker.bitjam.in";
    public static final int CALENDAR_PERMISSION_REQUEST_CODE = 379;

    public static final String[] PERMISSIONS = {
        Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};

    public static final String EVENT_ID = "EVENT_ID";
    public static final String RECORD_ID = "RECORD_ID";

    public static final int RECORD_INPUT_VALID = 0;
    public static final int RECORD_INPUT_EMPTY_NAME = 1;
    public static final int RECORD_INPUT_INVALID_DATE = 2;

    public static final int DIALOG_PERMISSION = 10;
    public static final int DIALOG_DELETE_CONFIRMATION = 11;
}
