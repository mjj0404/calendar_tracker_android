package com.example.calendartracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.example.calendartracker.Utility.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SignInActivity extends AppCompatActivity {

    private Button button;
    private GoogleSignInClient mGoogleSignInClient;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            handleSignInResult(task);

        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

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
//        try {
//            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//            updateUI(account);
//        } catch (Exception e) {
//            Log.d("TAGGG", "signInResult:failed code=" + e.getMessage());
//        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.d("TAGGG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        Log.d("TAGGG", "updateUI: " + account.getEmail());
        Log.d("TAGGG", "updateUI: " + account.getId());
        Log.d("TAGGG", "updateUI: " + account.getIdToken());
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }
}