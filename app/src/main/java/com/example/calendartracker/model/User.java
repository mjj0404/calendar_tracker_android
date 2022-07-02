package com.example.calendartracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("userid")
    @Expose
    private Integer userid;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("externid")
    @Expose
    private String externid;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExternid() {
        return externid;
    }

    public void setExternid(String externid) {
        this.externid = externid;
    }

}