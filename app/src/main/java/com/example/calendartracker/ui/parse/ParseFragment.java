package com.example.calendartracker.ui.parse;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.calendartracker.R;
import com.example.calendartracker.databinding.FragmentParseBinding;
import com.example.calendartracker.utility.CalendarQueryHandler;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.viewmodel.MainViewModel;


import java.util.Calendar;
import java.util.Map;

public class ParseFragment extends Fragment implements View.OnClickListener{

    private FragmentParseBinding binding;

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.CALENDAR_COLOR,                // 3
            CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,         // 4
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 5
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_COLOR = 3;
    private static final int PROJECTION_ACCESS_LEVEL = 4;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 5;

    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    Log.d("MAGG", "onActivityResult: " + result.keySet());
                    Log.d("MAGG", "onActivityResult: " + result.values());
                    Log.d("MAGG", "onActivityResult: " + viewModel.isPermissionGranted(result.values()));
                    if (viewModel.isPermissionGranted(result.values())) {
                        noPermissionLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                    }
                    else if (shouldShowRequestPermissionRationale(Constants.PERMISSIONS[0]) ||
                            shouldShowRequestPermissionRationale(Constants.PERMISSIONS[1])) {
                        Log.d("MAGG", "onActivityResult: elseif");
                    }
                    else {
                        Log.d("MAGG", "onActivityResult: else");
                    }

                }
            });

    private MainViewModel viewModel;
    private ConstraintLayout noPermissionLayout;
    private ConstraintLayout mainLayout;
    private Button askPermissionButton;
    private Button goToSettingButton;
    private Button testButton;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = FragmentParseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        noPermissionLayout = binding.parseNoPermissionLayout;
        mainLayout = binding.parseMainLayout;

        if (viewModel.isPermissionGranted(Constants.PERMISSIONS)) {
            noPermissionLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            Log.d("MAGG", "onViewCreated: permissionGranted");
        }
        else {
            requestPermissionLauncher.launch(Constants.PERMISSIONS);
        }
        viewModel.init();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        askPermissionButton = view.findViewById(R.id.parse_ask_permission_button);
        goToSettingButton = view.findViewById(R.id.parse_goto_setting_button);
        testButton = view.findViewById(R.id.parse_test_button);
        askPermissionButton.setOnClickListener(this);
        goToSettingButton.setOnClickListener(this);
        testButton.setOnClickListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.parse_ask_permission_button) {
            requestPermissionLauncher.launch(Constants.PERMISSIONS);
        }
        else if (view.getId() == R.id.parse_goto_setting_button) {
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
//            intent.setData(uri);
//            startActivity(intent);
        }
        else if (view.getId() == R.id.parse_test_button) {
//            addEvent("Hehe","",1657238400000l ,1657324800000l);

//            CalendarQueryHandler.insertEvent(getActivity(), startTime.getTimeInMillis(),
//                    endTime.getTimeInMillis(), "title");

            Calendar timeLong = Calendar.getInstance();
            timeLong.set(2022,6,13);



            CalendarQueryHandler.insertEvent(getActivity().getApplicationContext(),
                    timeLong.getTimeInMillis(), "some event");


        }
    }

    public void addEvent(String title, String location, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}