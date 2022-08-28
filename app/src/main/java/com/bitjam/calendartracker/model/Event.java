package com.bitjam.calendartracker.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
