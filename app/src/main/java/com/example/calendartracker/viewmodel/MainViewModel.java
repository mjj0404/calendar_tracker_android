package com.example.calendartracker.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.calendartracker.model.Lunar;
import com.example.calendartracker.model.Record;
import com.example.calendartracker.model.Solar;
import com.example.calendartracker.repo.RecordRepository;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.utility.LunarSolarConverter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private final Context context = getApplication().getApplicationContext();
    private RecordRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d("TAGGG", "MainViewModel: ");
    }

    public void init() {
        Log.d("TAGGG", "init: ");
        repository = new RecordRepository();
        getAllRecords();
        getUpcomingEventListLiveDataWithToken();
        getRecordListWithToken();

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
            Log.d("MAGG", "MainViewModel getUpcomingEventListWithToken: " + account.getId());
            repository.getAllRecord();
        }
    }

    public LiveData<List<Record>> getUpcomingEventListLiveData() {
        Log.d("MAGG", "MainViewModel getUpcomingEventListLiveData: ");
        return repository.getUpcomingEventListLiveData();
    }

    public void getUpcomingEventListLiveDataWithToken() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplication());
        if (account != null) {
            Log.d("MAGG", "MainViewModel getUpcomingEventListWithToken: " + account.getIdToken());
            repository.getUpcomingEventListLiveDataWithToken("Bearer " + account.getIdToken());
        }
    }

    public LiveData<List<Record>> getRecordListLiveData() {
        return repository.getRecordListLiveData();
    }

    public void getRecordListWithToken() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplication());
        if (account != null) {
            repository.getRecordListWithToken("Bearer " + account.getIdToken());
        }
    }

    public LiveData<Record> getRecordLiveData() {
        return repository.getRecordLiveData();
    }

    public void getRecord(int recordid) {
        repository.getRecord(recordid);
    }

    public void setRecord() {

    }

    public void updateRecord() {

    }

    public List<Integer> inputValidation (String name, String date, boolean isLeap) {
        List<Integer> result = new ArrayList<>();
        if (name.isEmpty()) result.add(Constants.RECORD_INPUT_EMPTY_NAME);

        if (date.length() == 8) {
            Lunar lunar = new Lunar(Integer.parseInt(date.substring(0, 4)),
                    Integer.parseInt(date.substring(4, 6)),
                    Integer.parseInt(date.substring(6)),
                    isLeap);
            Lunar lunarRecalculated = LunarSolarConverter.SolarToLunar(LunarSolarConverter.LunarToSolar(lunar));

            if (!lunar.equals(lunarRecalculated)) {
                result.add(Constants.RECORD_INPUT_INVALID_DATE);
            }
        }
        else {
            result.add(Constants.RECORD_INPUT_INVALID_DATE);
        }
        if (result.isEmpty()) result.add(Constants.RECORD_INPUT_VALID);
        return result;
    }



    public boolean isPermissionGranted(String[] strings) {
        for (String permissionString:strings) {
            Log.d("MAGG", "isPermissionGranted: " + permissionString);
            if (!(ContextCompat.checkSelfPermission(context, permissionString) ==
                    PackageManager.PERMISSION_GRANTED)) return false;
        }
        return true;
    }

    public boolean isPermissionGranted(Collection<Boolean> results) {
        int count = (int) results.stream().filter(aBoolean -> aBoolean).count();
        return count == results.size();
    }
}
