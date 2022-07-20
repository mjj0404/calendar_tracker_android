package com.example.calendartracker.ui.onboarding;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.leanback.app.OnboardingSupportFragment;

public class OnboardingManager extends OnboardingSupportFragment {

    @Override
    protected int getPageCount() {
        return 0;
    }

    @Override
    protected CharSequence getPageTitle(int pageIndex) {
        return null;
    }

    @Override
    protected CharSequence getPageDescription(int pageIndex) {
        return null;
    }

    @Nullable
    @Override
    protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Nullable
    @Override
    protected View onCreateForegroundView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }
}
