package com.example.calendartracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.calendartracker.api.ApiClient;
import com.example.calendartracker.api.ApiInterface;
import com.example.calendartracker.model.Record;
import com.example.calendartracker.utility.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private Button button;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "MainActivity SignInActivity";

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d(TAG, "onActivityResult: ");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            handleSignInResult(task);

        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Objects.requireNonNull(getSupportActionBar()).hide();

//        ApiClient.getInstance().getService().getRecords().enqueue(new Callback<List<Record>>() {
//            @Override
//            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
//                Log.d(TAG, "onResponse: ");
//            }
//
//            @Override
//            public void onFailure(Call<List<Record>> call, Throwable t) {
//                Log.d(TAG, "onFailure: ");
//            }
//        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(
                        this,
                        new OnCompleteListener<GoogleSignInAccount>() {
                            @Override
                            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                Log.d(TAG, "onComplete: silent sign in");
                                handleSignInResult(task);
                            }
                        });

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch(mGoogleSignInClient.getSignInIntent());
            }
        });

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                // go to next MainActivity
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // if getLastSignedInAccount returns non-null user is already signed in
        try {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            Log.d(TAG, "onStart: getLastSignedInAccount");
            updateUI(account);
        } catch (Exception e) {
            Log.d(TAG, "signInResult:failed code=" + e.getMessage());
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        Log.d(TAG, "updateUI: " + account.getEmail());
        Log.d(TAG, "updateUI: " + account.getId());
        Log.d(TAG, "updateUI: " + account.getIdToken());

        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }
}