package com.example.calendartracker.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calendartracker.utility.LunarSolarConverter;

import org.checkerframework.checker.units.qual.C;

import java.util.Calendar;

public class Event {

    private int year;
    private int month;
    private int day;

    public Event() { }

    public Event(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(this.year) + ',' +
                this.month + ',' +
                this.day;
    }

    public static Event upcomingEvent(int calendarid) {
        Calendar currentTime = Calendar.getInstance();

        Solar solar = LunarSolarConverter.solarFromInt((long) calendarid);
        Lunar lunar = LunarSolarConverter.solarToLunar(solar);
        lunar.lunarYear = currentTime.get(Calendar.YEAR);
        solar = LunarSolarConverter.lunarToSolar(lunar);

        Calendar recordTime = Calendar.getInstance();
        recordTime.set(solar.solarYear, solar.solarMonth - 1, solar.solarDay);
        Log.d("EventFragment", "upcomingEvent: recordTime :" + recordTime.get(Calendar.YEAR)
        + "," + recordTime.get(Calendar.MONTH)+1 + "," +  recordTime.get(Calendar.DATE));
        if (recordTime.before(currentTime)) {
            lunar.lunarYear = currentTime.get(Calendar.YEAR) + 1;
            solar = LunarSolarConverter.lunarToSolar(lunar);
            Log.d("EventFragment", "upcomingEvent: inside if : " + solar.solarYear+ "," +
                    solar.solarMonth+ "," + solar.solarDay);
        }
        Event event = new Event(solar.solarYear, solar.solarMonth, solar.solarDay);
        Log.d("EventFragment", "upcomingEvent: event : " + event.toString());
        return event;
    }

    public static Event fromString(String eventString) {
        String[] eventStringArray = eventString.split(",");
        int year = Integer.parseInt(eventStringArray[0]);
        int month = Integer.parseInt(eventStringArray[1]);
        int day = Integer.parseInt(eventStringArray[2]);
        return new Event(year, month, day);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj != null) {
            Event otherEvent = (Event) obj;
            return this.year == otherEvent.getYear() &&
                    this.month == otherEvent.getMonth() &&
                    this.day == otherEvent.getDay();
        }
        return false;
    }
}
