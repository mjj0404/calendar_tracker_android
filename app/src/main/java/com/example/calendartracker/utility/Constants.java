package com.example.calendartracker.utility;

import android.Manifest;

public class Constants {
    public static final String BASE_URL = "https://calendar-tracker.bitjam.in";
    public static final int CALENDAR_PERMISSION_REQUEST_CODE = 379;

    public static final String[] PERMISSIONS = {
        Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};

    public static final String ID_MAP_RECORD_EVENT = "ID_MAP_RECORD_EVENT";
    public static final String RECORD_ID = "RECORD_ID";

    public static final int RECORD_INPUT_VALID = 0;
    public static final int RECORD_INPUT_EMPTY_NAME = 1;
    public static final int RECORD_INPUT_INVALID_DATE = 2;

    public static final int DIALOG_PERMISSION_SHORT = 9;
    public static final int DIALOG_PERMISSION_LONG = 10;
    public static final int DIALOG_CREATE_EVENT = 11;
    public static final int DIALOG_DELETE_CONFIRMATION = 12;
}
