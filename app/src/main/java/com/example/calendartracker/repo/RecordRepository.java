package com.example.calendartracker.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.calendartracker.api.ApiInterface;
import com.example.calendartracker.model.Record;

import java.util.List;

public class RecordRepository {

    private ApiInterface apiService;
    private MutableLiveData<List<Record>> recordListLiveData;

    public RecordRepository() {

    }

    public LiveData<List<Record>> getRecordList() {
        return recordListLiveData;
    }
}
