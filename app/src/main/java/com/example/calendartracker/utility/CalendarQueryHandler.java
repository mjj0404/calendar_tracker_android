package com.example.calendartracker.utility;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.*;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarQueryHandler extends AsyncQueryHandler {
    private static final String TAG = "CalendarQueryHandler";

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

    // The indices for the projection array above.
    private static final int CALENDAR_ID_INDEX = 0;

    private static final int INSERT = 0;
    private static final int UPDATE = 1;
    private static final int EVENT    = 2;
    private static final int REMINDER = 3;

    private static CalendarQueryHandler CalendarQueryHandler;

    // CalendarQueryHandler
    public CalendarQueryHandler(ContentResolver resolver)
    {
        super(resolver);
    }

    // insertEvent
    public static void insertEvent(Context context, long timeLong, String title)
    {
        ContentResolver resolver = context.getContentResolver();

        if (CalendarQueryHandler == null)
            CalendarQueryHandler = new CalendarQueryHandler(resolver);

        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, timeLong);
        values.put(Events.DTEND, timeLong);
        values.put(Events.ALL_DAY, 1);
        values.put(Events.TITLE, title);
        values.put(Events.DESCRIPTION, "Group workout");

        CalendarQueryHandler.startQuery(INSERT, values, Calendars.CONTENT_URI,
                CALENDAR_PROJECTION, null, null, null);
    }

    public static void updateEvent(Context context, long timeLong, String title) {
        ContentResolver resolver = context.getContentResolver();

        if (CalendarQueryHandler == null)
            CalendarQueryHandler = new CalendarQueryHandler(resolver);

        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, timeLong);
        values.put(Events.DTEND, timeLong);
        values.put(Events.ALL_DAY, 1);
        values.put(Events.TITLE, title);
        values.put(Events.DESCRIPTION, "Group workout");

        PreferenceManager.init(context);

        StringBuilder builder = new StringBuilder();
        builder.append(values.get(Events.TITLE)).append(values.get(Events.DTSTART));

        CalendarQueryHandler.startQuery(UPDATE, values, Calendars.CONTENT_URI,
                CALENDAR_PROJECTION, null, null, null);
    }

    // onQueryComplete
    @Override
    public void onQueryComplete(int token, Object object, Cursor cursor)
    {
        if (token == INSERT) {

            // Use the cursor to move through the returned records
            cursor.moveToFirst();

            // Get the field values
            long calendarID = cursor.getLong(CALENDAR_ID_INDEX);

            ContentValues values = (ContentValues) object;
            values.put(Events.CALENDAR_ID, calendarID);
            values.put(Events.EVENT_TIMEZONE,
                    TimeZone.getDefault().getDisplayName());

            Log.d("MAGG", "onQueryComplete: calendarId: " + calendarID
                    + "\nvalues.get from insert: " + values.get(Events.DTSTART));

            startInsert(EVENT, null, Events.CONTENT_URI, values);
        }
        else if (token == UPDATE) {

            ContentValues values = (ContentValues) object;
//            values.put(Events.CALENDAR_ID, calendarID);
            values.put(Events.EVENT_TIMEZONE,
                    TimeZone.getDefault().getDisplayName());

//            StringBuilder builder = new StringBuilder();
//            builder.append(calendarID).append(values.get(Events.TITLE)).append(values.get(Events.DTSTART));

//            String selection = "(" + Calendars._ID + "= ?)";
//            String[] selectionArgs = new String[] {String.valueOf(calendarID)};

//            startUpdate(EVENT, null, Events.CONTENT_URI, values, selection, selectionArgs);

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
                    long eventID = Long.parseLong(uri.getLastPathSegment());
                    // TODO : give users to set reminder
                    ContentValues values = (ContentValues) object;
//                    ContentValues values = new ContentValues();
//                    values.put(Reminders.MINUTES, 10);
//                    values.put(Reminders.EVENT_ID, eventID);
//                    values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
//                    startInsert(REMINDER, null, Reminders.CONTENT_URI, values);
                    break;
            }
        }
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        super.onUpdateComplete(token, cookie, result);
    }
}
