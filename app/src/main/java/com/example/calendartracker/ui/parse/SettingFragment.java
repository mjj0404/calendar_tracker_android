package com.example.calendartracker.ui.parse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.calendartracker.R;
import com.example.calendartracker.databinding.FragmentSettingBinding;
import com.example.calendartracker.model.Lunar;
import com.example.calendartracker.model.Record;
import com.example.calendartracker.model.Solar;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.utility.AlertDialogWithListener;
import com.example.calendartracker.utility.LunarSolarConverter;
import com.example.calendartracker.viewmodel.MainViewModel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private static final String TAG = "SettingFragment";

    private MainViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel.init();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}