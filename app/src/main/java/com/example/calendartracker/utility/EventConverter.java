package com.example.calendartracker.utility;

import android.util.Log;

import com.example.calendartracker.model.Event;
import com.example.calendartracker.model.Lunar;
import com.example.calendartracker.model.Solar;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class EventConverter {
    public static Event originalEvent(int calendarid) {
        Solar solar = LunarSolarConverter.solarFromInt(calendarid);
        LocalDate localDate = LocalDate.of(solar.solarYear, solar.solarMonth, solar.solarDay);
        return new Event(solar.solarYear, solar.solarMonth, solar.solarDay);
    }

    public static Event upcomingEvent(int calendarid) {
        Calendar currentTime = Calendar.getInstance();

        Solar solar = LunarSolarConverter.solarFromInt((long) calendarid);
        Lunar lunar = LunarSolarConverter.solarToLunar(solar);
        lunar.lunarYear = currentTime.get(Calendar.YEAR);
        solar = LunarSolarConverter.lunarToSolar(lunar);

        Calendar recordTime = Calendar.getInstance();
        recordTime.set(solar.solarYear, solar.solarMonth - 1, solar.solarDay);

        if (recordTime.before(currentTime)) {
            lunar.lunarYear = currentTime.get(Calendar.YEAR) + 1;
            solar = LunarSolarConverter.lunarToSolar(lunar);

        }
        return fromSolar(solar);
    }

    public static LocalDate toInstant(Event event) {
        return LocalDate.of(event.getYear(), event.getMonth(), event.getDay());
    }

    public static LocalDate toInstant(Solar solar) {
        return LocalDate.of(solar.solarYear, solar.solarMonth, solar.solarDay);
    }

    public static LocalDate toInstant(Lunar lunar) {
        Solar solar = LunarSolarConverter.lunarToSolar(lunar);
        return LocalDate.of(solar.solarYear, solar.solarMonth, solar.solarDay);
    }

    public static Lunar toLunar(Event event) {
        long calendarid = LunarSolarConverter.solarToInt(event.getYear(), event.getMonth(), event.getDay());
        return LunarSolarConverter.solarToLunar(LunarSolarConverter.solarFromInt(calendarid));
    }

    public static Calendar toCalendar(Event event) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(event.getYear(), event.getMonth()-1, event.getDay());
        return calendar;
    }

    public static Event fromCalendar(Calendar calendar) {
        return new Event(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE));
    }

    public static Event fromCalendarId(int calendarid) {
        return fromSolar(LunarSolarConverter.solarFromInt(calendarid));
    }

    public static Event fromSolar(Solar solar) {
        return new Event(solar.solarYear, solar.solarMonth, solar.solarDay);
    }

    public static Event fromString(String eventString) {
        String[] eventStringArray = eventString.split(",");
        int year = Integer.parseInt(eventStringArray[0]);
        int month = Integer.parseInt(eventStringArray[1]);
        int day = Integer.parseInt(eventStringArray[2]);
        return new Event(year, month, day);
    }
}
