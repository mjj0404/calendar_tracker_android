package com.example.calendartracker.repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.calendartracker.api.ApiClient;
import com.example.calendartracker.model.Record;
import com.google.android.gms.common.api.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordRepository {

    private MutableLiveData<List<Record>> allRecordList = new MutableLiveData<>();
    private MutableLiveData<List<Record>> upcomingEventListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Record>> recordListLiveData = new MutableLiveData<>();
    private MutableLiveData<Record> record = new MutableLiveData<>();

    public RecordRepository () {}

    public LiveData<List<Record>> getAllRecordLiveData() {
        Log.d("MAGG", "Repository getUpcomingEventListLiveData: ");
        return allRecordList;
    }

    public LiveData<List<Record>> getUpcomingEventListLiveData() {
        Log.d("MAGG", "Repository getUpcomingEventListLiveData: ");
        return upcomingEventListLiveData;
    }

    public void getUpcomingEventListLiveDataWithToken(String idToken) {
        ApiClient.getInstance().getService().getUserRecordByLunarDate(idToken).enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                if (response.body() != null) {
                    Log.d("MAGG", "onResponse: ");
                    upcomingEventListLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Record>> call, Throwable t) {
                Log.d("MAGG", "onFailure: ");
            }
        });
    }

    public LiveData<List<Record>> getRecordListLiveData() {
        return recordListLiveData;
    }

    public void getRecordListWithToken(String idToken) {
        ApiClient.getInstance().getService().getUserRecord(idToken).enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                if (response.body() != null) {
                    recordListLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Record>> call, Throwable t) {
                recordListLiveData.postValue(null);
            }
        });
    }

    public LiveData<Record> getRecordLiveData() {
        return record;
    }

    public void getRecord(int recordid) {
        ApiClient.getInstance().getService().getRecord(recordid).enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Call<Record> call, Response<Record> response) {
                record.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Record> call, Throwable t) {

            }
        });
    }

    //==================================UPDATE=================================================

    public void updateRecord(int recordid, String name, int calendarid) {
        ApiClient.getInstance().getService().updateRecord(recordid, name, calendarid).enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Call<Record> call, Response<Record> response) {

            }

            @Override
            public void onFailure(Call<Record> call, Throwable t) {

            }
        });
    }

    //==================================CREATE=================================================

    public void createRecord( String externid, String name, int calendarid) {
        ApiClient.getInstance().getService().createRecord(externid, name, calendarid).enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Call<Record> call, Response<Record> response) {

            }

            @Override
            public void onFailure(Call<Record> call, Throwable t) {

            }
        });
    }
}
