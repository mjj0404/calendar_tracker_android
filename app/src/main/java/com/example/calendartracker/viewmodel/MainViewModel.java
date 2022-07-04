package com.example.calendartracker.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.calendartracker.model.Record;
import com.example.calendartracker.repo.RecordRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private RecordRepository repository;
    private LiveData<List<Record>> allRecordListLiveData;
//    private LiveData<List<Record>> upcomingEventListLiveData;
//    private LiveData<List<Record>> recordListLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d("TAGGG", "MainViewModel: ");
    }

    public void init() {
        Log.d("TAGGG", "init: ");
        repository = new RecordRepository();
        getAllRecords();
//        getUpcomingEventListWithToken();
//        getRecordListWithToken();
//        upcomingEventListLiveData = repository.getUpcomingEventListLiveData();
//        recordListLiveData = repository.getRecordListLiveData();
    }

    public LiveData<List<Record>> getAllRecordListLiveData() {
        Log.d("MAGG", "MainViewModel getUpcomingEventListLiveData: ");
        return repository.getAllRecordLiveData();
    }

    public void getAllRecords() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplication());
        if (account != null) {
            Log.d("MAGG", "MainViewModel getUpcomingEventListWithToken: " + account.getIdToken());
            repository.getAllRecord();
        }
    }

//    public LiveData<List<Record>> getUpcomingEventListLiveData() {
//        Log.d("MAGG", "MainViewModel getUpcomingEventListLiveData: ");
//        return upcomingEventListLiveData;
//    }
//
//    public void getUpcomingEventListWithToken() {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplication());
//        if (account != null) {
//            Log.d("MAGG", "MainViewModel getUpcomingEventListWithToken: " + account.getIdToken());
//            repository.getUpcomingEventListWithToken("Bearer" + account.getIdToken());
//        }
//    }

//    public LiveData<List<Record>> getRecordListLiveData() {
//        return recordListLiveData;
//    }
//
//    public void getRecordListWithToken() {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplication());
//        if (account != null) {
//            repository.getRecordListWithToken("Bearer" + account.getIdToken());
//        }
//    }
}
