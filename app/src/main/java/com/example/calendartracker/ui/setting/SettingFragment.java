package com.example.calendartracker.ui.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.calendartracker.CalendarTrackerApplication;
import com.example.calendartracker.MainActivity;
import com.example.calendartracker.R;
import com.example.calendartracker.databinding.FragmentSettingBinding;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.utility.PreferenceManager;
import com.example.calendartracker.viewmodel.MainViewModel;

import java.util.Objects;

public class SettingFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    private FragmentSettingBinding binding;
    private static final String TAG = "SettingFragment";

    private RadioGroup themeRadioGroup;
    private RadioGroup reminderRadioGroup;
    private Switch remindSwitch;
    private LinearLayout remindLayout;

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
        themeRadioGroup = view.findViewById(R.id.setting_theme_radiogroup);
        reminderRadioGroup = view.findViewById(R.id.setting_remind_me_before_radiogroup);
        remindSwitch = view.findViewById(R.id.setting_reminder_switch);
        remindLayout = view.findViewById(R.id.setting_reminder_layout);

        themeRadioGroup.setOnCheckedChangeListener(this);
        reminderRadioGroup.setOnCheckedChangeListener(this);
        remindSwitch.setOnCheckedChangeListener(this);

        setThemeRadioGroup();
        setEventUploadView();

//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                Log.d(TAG, "handleOnBackPressed: ");
//                requireActivity().finish();
//                System.exit(0);
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int groupId = radioGroup.getId();
        int checkedId = radioGroup.getCheckedRadioButtonId();
        if (groupId == R.id.setting_theme_radiogroup) {
            // if theme changed
            switch (checkedId) {
                case R.id.setting_system_theme_radiobutton:
                    CalendarTrackerApplication.setApplicationTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    Log.d(TAG, "onCheckedChanged: ");
                    break;
                case R.id.setting_light_theme_radiobutton:
                    CalendarTrackerApplication.setApplicationTheme(AppCompatDelegate.MODE_NIGHT_NO);
                    Log.d(TAG, "onCheckedChanged: ");
                    break;
                case R.id.setting_dark_theme_radiobutton:
                    CalendarTrackerApplication.setApplicationTheme(AppCompatDelegate.MODE_NIGHT_YES);
                    Log.d(TAG, "onCheckedChanged: ");
                    break;
            }
        }
        else if (groupId == R.id.setting_remind_me_before_radiogroup) {
            switch (checkedId) {
                case R.id.setting_before_day_radiobutton:
                    PreferenceManager.getInstance().setReminderSetting(Constants.REMIND_DAY_BEFORE);
                    break;
                case R.id.setting_before_hour_radiobutton:
                    PreferenceManager.getInstance().setReminderSetting(Constants.REMIND_HOUR_BEFORE);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.setting_reminder_switch) {
            if (b) {
                remindLayout.setVisibility(View.VISIBLE);
                switch (PreferenceManager.getInstance().getReminderSetting()) {
                    case Constants.REMIND_DAY_BEFORE:
                        reminderRadioGroup.check(R.id.setting_before_day_radiobutton);
                        break;
                    case Constants.REMIND_HOUR_BEFORE:
                        reminderRadioGroup.check(R.id.setting_before_hour_radiobutton);
                }
            }
            else {
                remindLayout.setVisibility(View.GONE);
            }
            PreferenceManager.getInstance().addReminder(b);
        }
    }

    private void setThemeRadioGroup() {

        switch (PreferenceManager.getInstance().getTheme()) {
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                themeRadioGroup.check(R.id.setting_system_theme_radiobutton);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                themeRadioGroup.check(R.id.setting_light_theme_radiobutton);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                themeRadioGroup.check(R.id.setting_dark_theme_radiobutton);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: MainActivity");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @SuppressLint("NonConstantResourceId")
    private void setEventUploadView() {
        // TODO : REMINDER || REMINDER_SETTING
        remindSwitch.setChecked(PreferenceManager.getInstance().isAddingReminder());
        if (PreferenceManager.getInstance().isAddingReminder()) {
            remindLayout.setVisibility(View.VISIBLE);
        } else {
            remindLayout.setVisibility(View.GONE);
        }

        switch (PreferenceManager.getInstance().getReminderSetting()) {
            case Constants.REMIND_DAY_BEFORE:
                reminderRadioGroup.check(R.id.setting_before_day_radiobutton);
                break;
            case Constants.REMIND_HOUR_BEFORE:
                reminderRadioGroup.check(R.id.setting_before_hour_radiobutton);
                break;
        }
    }
}