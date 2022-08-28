package com.bitjam.calendartracker.utility;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.*;

import com.bitjam.calendartracker.R;

import java.util.TimeZone;

public class CalendarQueryHandler extends AsyncQueryHandler {
    private static final String TAG = "CalendarQueryHandler";

    public interface OnQueryFinishListener {
        void onQueryFinished(String name);
    }

    // Projection arrays
    private static final String[] CALENDAR_PROJECTION = new String[]
            {
                    CalendarContract.Calendars._ID,                           // 0
                    CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                    CalendarContract.Calendars.CALENDAR_COLOR,                // 3
                    CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,         // 4
                    CalendarContract.Calendars.OWNER_ACCOUNT                  // 5
            };

    private OnQueryFinishListener listener;
    private static final int CALENDAR_ID_INDEX = 0;

    private static final int INSERT = 0;
    private static final int EVENT = 2;
    private static final int REMINDER = 3;

    private static CalendarQueryHandler CalendarQueryHandler;

    // CalendarQueryHandler
    public CalendarQueryHandler(ContentResolver resolver, OnQueryFinishListener listener)
    {
        super(resolver);
        this.listener = listener;
    }

    // insertEvent
    public static void insertEvent(Context context, long timeLong, String title, OnQueryFinishListener listener)
    {
        ContentResolver resolver = context.getContentResolver();

        if (CalendarQueryHandler == null)
            CalendarQueryHandler = new CalendarQueryHandler(resolver, listener);
        listener.onQueryFinished(title);

        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, timeLong);
        values.put(Events.DTEND, timeLong);
        values.put(Events.ALL_DAY, 1);
        values.put(Events.TITLE, title);
        values.put(Events.DESCRIPTION, "Group workout");

        CalendarQueryHandler.startQuery(INSERT, values, Calendars.CONTENT_URI,
                CALENDAR_PROJECTION, null, null, null);
    }

    // onQueryComplete
    @Override
    public void onQueryComplete(int token, Object object, Cursor cursor)
    {
        if (token == INSERT) {
            cursor.moveToFirst();
            long calendarID = cursor.getLong(CALENDAR_ID_INDEX);

            ContentValues values = (ContentValues) object;
            values.put(Events.CALENDAR_ID, calendarID);
            values.put(Events.EVENT_TIMEZONE,
                    TimeZone.getDefault().getDisplayName());

            startInsert(EVENT, null, Events.CONTENT_URI, values);
        }
    }

    // onInsertComplete
    @Override
    public void onInsertComplete(int token, Object object, Uri uri)
    {
        if (uri != null)
        {
            switch (token)
            {
                case EVENT:
                    int reminderSetting = PreferenceManager.getInstance().getReminderSetting();
                    long eventID = Long.parseLong(uri.getLastPathSegment());
                    ContentValues values = (ContentValues) object;

                    if (PreferenceManager.getInstance().isAddingReminder()) {
                        if (reminderSetting == Constants.REMIND_DAY_BEFORE) {
                            values.put(Reminders.MINUTES, 1440);
                        }
                        else if (reminderSetting == Constants.REMIND_HOUR_BEFORE) {
                            values.put(Reminders.MINUTES, 60);
                        }
                        values.put(Reminders.EVENT_ID, eventID);
                        values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
                        startInsert(REMINDER, null, Reminders.CONTENT_URI, values);
                    }
                    break;
            }
        }
    }
}
