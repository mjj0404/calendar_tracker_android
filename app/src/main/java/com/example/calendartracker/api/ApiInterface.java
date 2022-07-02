package com.example.calendartracker.api;

import com.example.calendartracker.model.Record;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface{

    @GET("/record")
    Call<List<Record>> getRecords();
}
