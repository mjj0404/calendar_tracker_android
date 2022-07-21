package com.example.calendartracker.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.calendartracker.CalendarTrackerApplication;
import com.example.calendartracker.R;
import com.example.calendartracker.SignInActivity;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.utility.PreferenceManager;

import java.util.Objects;

public class OnboardingActivity extends AppCompatActivity {

    private static final String TAG = "OnboardingActivity";

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (!PreferenceManager.getInstance().isFirstTime()) {
            Intent intent = new Intent(OnboardingActivity.this, SignInActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }

        viewPager = findViewById(R.id.onboarding_view_pager);
        pagerAdapter = new OnboardingPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }


}