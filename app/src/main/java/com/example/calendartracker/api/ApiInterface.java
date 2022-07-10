package com.example.calendartracker.api;

import com.example.calendartracker.model.Record;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface ApiInterface{

    @GET("/record")
    Call<List<Record>> getRecords();

    @GET("/me/event")
    Call<List<Record>> getUserRecordByLunarDate(@Header("Authorization") String auth);

    @GET("/me")
    Call<List<Record>> getUserRecord(@Header("Authorization") String auth);

}
