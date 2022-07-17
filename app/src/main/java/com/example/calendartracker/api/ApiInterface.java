package com.example.calendartracker.api;

import com.example.calendartracker.model.Record;
import com.example.calendartracker.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface{

    //==================================DELETE=================================================

    @DELETE("/user/delete/{externid}")
    Call<User> deleteUser(@Path("externid") String externid);

    @DELETE("/record/delete/{recordid}")
    Call<Record> deleteRecord(@Path("recordid") int recordid);

    //==================================CREATE=================================================

    @FormUrlEncoded
    @POST("/user/create")
    Call<User> createUser(@Field("email") String email,
                          @Field("externid") String externid);

    @FormUrlEncoded
    @POST("/record/create")
    Call<Record> createRecord(@Field("externid") String externid,
                              @Field("name") String name,
                              @Field("calendarid") int calendarid);

    //==================================UPDATE=================================================

    @FormUrlEncoded
    @POST("/record/update/{recordid}")
    Call<Record> updateRecord(@Path("recordid") int recordid,
                              @Field("name") String name,
                              @Field("calendarid") int calendarid);

    //====================================GET=================================================

    @GET("/record/{recordid}")
    Call<Record> getRecord(@Path("recordid") int recordid);

    @GET("/me/event")
    Call<List<Record>> getUserRecordByLunarDate(@Header("Authorization") String auth);

    @GET("/me")
    Call<List<Record>> getUserRecord(@Header("Authorization") String auth);

}
