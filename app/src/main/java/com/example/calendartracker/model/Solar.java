package com.example.calendartracker.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Solar {
    public int solarYear;
    public int solarMonth;
    public int solarDay;

    public Solar() {}

    public Solar(int solarYear, int solarMonth, int solarDay) {
        this.solarYear = solarYear;
        this.solarMonth = solarMonth;
        this.solarDay = solarDay;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ((obj instanceof Solar) &&
        this.solarYear == ((Solar) obj).solarYear &&
        this.solarMonth == ((Solar) obj).solarMonth &&
        this.solarDay == ((Solar) obj).solarDay);
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.valueOf(this.solarYear) +
                String.format("%02d", this.solarMonth) +
                String.format("%02d", this.solarDay);
    }
}
