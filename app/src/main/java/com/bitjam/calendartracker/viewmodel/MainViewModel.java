package com.bitjam.calendartracker.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bitjam.calendartracker.R;
import com.bitjam.calendartracker.api.ApiClient;
import com.bitjam.calendartracker.model.Event;
import com.bitjam.calendartracker.model.Record;
import com.bitjam.calendartracker.model.User;
import com.bitjam.calendartracker.repo.RecordRepository;
import com.bitjam.calendartracker.utility.CalendarQueryHandler;
import com.bitjam.calendartracker.utility.EventConverter;
import com.bitjam.calendartracker.utility.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "ViewModel";

    @SuppressLint("StaticFieldLeak")
    private final Context context = getApplication().getApplicationContext();
    private RecordRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        repository = new RecordRepository();
        getUpcomingEventListLiveDataWithToken();
        getRecordListWithToken();
    }

    public LiveData<List<Record>> getAllRecordListLiveData() {
        return repository.getAllRecordLiveData();
    }

    public LiveData<List<Record>> getUpcomingEventListLiveData() {
        return repository.getUpcomingEventListLiveData();
    }

    public void getUpcomingEventListLiveDataWithToken() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplication());
        if (account != null) {
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

    public void queryUpcomingEventList(List<Record> recordList,
                                       CalendarQueryHandler.OnQueryFinishListener listener) {
        Map<String, String> hashMap = PreferenceManager.getInstance().loadEventHashMap();
        recordList.forEach(record -> {
            Event upcomingEvent = EventConverter.upcomingEvent(record.getCalendarid());
            Calendar upcomingEventCalendar = EventConverter.toCalendar(upcomingEvent);

            if (hashMap.containsKey(record.toString())) {
                String value = hashMap.get(record.toString());
                Event eventValue = EventConverter.fromString(Objects.requireNonNull(value));
                if (!upcomingEvent.equals(eventValue)) {
                    hashMap.put(record.toString(), upcomingEvent.toString());
                    CalendarQueryHandler.insertEvent(context,
                            upcomingEventCalendar.getTimeInMillis(),
                            record.getName(),
                            listener);
                }
            }
            else {
                //put new value to hashmap and insert query
                hashMap.put(record.toString(), upcomingEvent.toString());
                CalendarQueryHandler.insertEvent(context,
                        upcomingEventCalendar.getTimeInMillis(),
                        record.getName(),
                        listener);
            }
        });
        PreferenceManager.getInstance().storeEventHashMap(hashMap);
    }

    public GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.server_client_id))
                .requestEmail()
                .build();
    }

    //==================================DELETE=================================================

    public void deleteUser() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        ApiClient.getInstance().getService().deleteUser(account.getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    public void deleteRecord(int recordid) {
        ApiClient.getInstance().getService().deleteRecord(recordid).enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Call<Record> call, Response<Record> response) {
            }

            @Override
            public void onFailure(Call<Record> call, Throwable t) {

            }
        });
    }

    //==================================CREATE=================================================

    public void createUser() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        ApiClient.getInstance().getService().createUser(account.getEmail(), account.getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    public void createRecord(String name, int calendarid) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        ApiClient.getInstance().getService().createRecord(account.getId(), name, calendarid).enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Call<Record> call, Response<Record> response) {

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
}
