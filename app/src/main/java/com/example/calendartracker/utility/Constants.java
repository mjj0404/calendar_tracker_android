package com.example.calendartracker.utility;

import android.Manifest;

public class Constants {
    public static final String BASE_URL = "https://calendar-tracker.bitjam.in";
    public static final int CALENDAR_PERMISSION_REQUEST_CODE = 379;
    public static final String INITIAL_SIGN_IN = "INITIAL_SIGN_IN";

    public static final String[] PERMISSIONS = {
        Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};

    public static final String ID_MAP_RECORD_EVENT = "ID_MAP_RECORD_EVENT";
    public static final String RECORD_ID = "RECORD_ID";
    public static final int MAX_PAGES = 4;

    public static final int RECORD_INPUT_VALID = 0;
    public static final int RECORD_INPUT_EMPTY_NAME = 1;
    public static final int RECORD_INPUT_INVALID_DATE = 2;

    public static final int DIALOG_PERMISSION_SHORT = 9;
    public static final int DIALOG_PERMISSION_LONG = 10;
    public static final int DIALOG_CREATE_EVENT = 11;
    public static final int DIALOG_DELETE_CONFIRMATION = 12;
    public static final int DIALOG_PARSE_SINGLE_RECORD = 13;

    public static final String DIALOG_DO_NOT_ASK = "DIALOG_DO_NOT_ASK";

    // ================================SETTING===============================================

    public static final String ADD_REMINDER_SELECTION = "ADD_REMINDER_SELECTION";

    public static final String THEME_SELECTION = "THEME_SELECTION";
    public static final String THEME_CONFIGURATION_CHANGED = "THEME_CONFIGURATION_CHANGED";

    public static final String BEFORE_EVENT = "BEFORE_EVENT";
    public static final int REMIND_DAY_BEFORE = 0;
    public static final int REMIND_HOUR_BEFORE = 1;

    public static final String FIRST_TIME = "FIRST_TIME";
}
