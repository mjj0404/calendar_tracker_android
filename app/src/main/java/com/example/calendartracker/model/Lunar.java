package com.example.calendartracker.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calendartracker.R;

public class Lunar {
    public int lunarYear;
    public int lunarMonth;
    public int lunarDay;
    public boolean isleap;

    public Lunar() {}

    public Lunar(int lunarYear, int lunarMonth, int lunarDay, boolean isleap) {
        this.lunarYear = lunarYear;
        this.lunarMonth = lunarMonth;
        this.lunarDay = lunarDay;
        this.isleap = isleap;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ((obj instanceof Lunar) &&
        this.lunarYear == ((Lunar) obj).lunarYear &&
        this.lunarMonth == ((Lunar) obj).lunarMonth &&
        this.lunarDay == ((Lunar) obj).lunarDay &&
        this.isleap == ((Lunar) obj).isleap);
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.valueOf(this.lunarYear) +
                String.format("%02d", this.lunarMonth) +
                String.format("%02d", this.lunarDay);
    }
}
