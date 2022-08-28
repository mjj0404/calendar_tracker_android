package com.bitjam.calendartracker.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("recordid")
    @Expose
    private Integer recordid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("calendarid")
    @Expose
    private Integer calendarid;
    @SerializedName("externid")
    @Expose
    private String externid;

    public Integer getRecordid() {
        return recordid;
    }

    public void setRecordid(Integer recordid) {
        this.recordid = recordid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCalendarid() {
        return calendarid;
    }

    public void setCalendarid(Integer calendarid) {
        this.calendarid = calendarid;
    }

    public String getExternid() {
        return externid;
    }

    public void setExternid(String externid) {
        this.externid = externid;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.recordid).append(this.name)
                .append(this.calendarid);
        return builder.toString();
    }
}