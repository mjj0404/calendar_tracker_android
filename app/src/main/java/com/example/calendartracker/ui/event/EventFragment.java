package com.example.calendartracker.ui.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartracker.R;
import com.example.calendartracker.databinding.FragmentEventBinding;
import com.example.calendartracker.model.Lunar;
import com.example.calendartracker.model.Record;
import com.example.calendartracker.model.Solar;
import com.example.calendartracker.utility.AlertDialogWithListener;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.utility.LunarSolarConverter;
import com.example.calendartracker.utility.PreferenceManager;
import com.example.calendartracker.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EventFragment extends Fragment {

    private FragmentEventBinding binding;
    private MainViewModel viewModel;
    private EventAdapter adapter;
    private List<Record> upcomingEventList = new ArrayList<>();

    private static final String TAG = "EventFragment";

    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    if (viewModel.isPermissionGranted(result.values())) {
                        AlertDialogWithListener dialog = new AlertDialogWithListener(
                                requireActivity(),
                                Constants.DIALOG_CREATE_EVENT,
                                new AlertDialogWithListener.DialogOnClickListener() {
                                    @Override
                                    public void onConfirmClick() {
                                        Log.d(TAG, "onConfirmClick: ");
                                        parseEventToGoogleCalendar();
                                    }

                                    @Override
                                    public void onCancelClick() {
                                        Log.d(TAG, "onCancelClick: ");
                                    }
                                }
                        );
                        dialog.onCreateDialog(null).show();
                    }
                    else if (shouldShowRequestPermissionRationale(Constants.PERMISSIONS[0]) ||
                            shouldShowRequestPermissionRationale(Constants.PERMISSIONS[1])) {
                        AlertDialogWithListener dialog = new AlertDialogWithListener(
                                requireActivity(),
                                Constants.DIALOG_PERMISSION_SHORT,
                                new AlertDialogWithListener.DialogOnClickListener() {
                                    @Override
                                    public void onConfirmClick() {
                                        Log.d(TAG, "onConfirmClick: onConfirm");
                                    }

                                    @Override
                                    public void onCancelClick() {
                                        Log.d(TAG, "onCancelClick: onCancel");
                                    }
                                }
                        );
                        dialog.onCreateDialog(null).show();
                    }
                    else if (!viewModel.isPermissionGranted(result.values())) {
                        Log.d("MAGG", "onActivityResult: else");
                        AlertDialogWithListener dialog = new AlertDialogWithListener(
                                requireActivity(),
                                Constants.DIALOG_PERMISSION_LONG,
                                new AlertDialogWithListener.DialogOnClickListener() {
                                    @Override
                                    public void onConfirmClick() {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelClick() { }
                                }
                        );
                        dialog.onCreateDialog(null).show();
                    }

                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("MAGG", "onCreateView: Dashboard Frag");
        binding = FragmentEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.init();

        viewModel.getUpcomingEventListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                adapter.setRecordList(records);
                upcomingEventList = records;
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new EventAdapter();
        RecyclerView recyclerView = binding.eventRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.appbar_event, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_parse) {
                    requestPermissionLauncher.launch(Constants.PERMISSIONS);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

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

    private void parseEventToGoogleCalendar() {

//            addEvent("Hehe","",1657238400000l ,1657324800000l);

//            CalendarQueryHandler.insertEvent(getActivity(), startTime.getTimeInMillis(),
//                    endTime.getTimeInMillis(), "title");


        Calendar timeLong = Calendar.getInstance();
        timeLong.set(2022,6,0);
        // month start from 0
        // day start from 1
        Log.d(TAG, "onClick: " + timeLong.getTimeInMillis());
        Log.d(TAG, "onClick: " + timeLong.getTime());
        Log.d(TAG, "onClick: " + timeLong.getTimeZone());

        Log.d(TAG, "onClick: " + upcomingEventList.toString());

        upcomingEventList.forEach(record -> {
            long calendarid = record.getCalendarid().longValue();
            Solar solar = LunarSolarConverter.SolarFromInt(calendarid);
            Lunar lunar = LunarSolarConverter.SolarToLunar(solar);

            Log.d(TAG, "onClick: " + Calendar.getInstance().get(Calendar.YEAR) + " " +
                    Calendar.getInstance().get(Calendar.MONTH) + " " +
                    Calendar.getInstance().get(Calendar.DATE));



            Calendar timeLongTemp = Calendar.getInstance();
            timeLongTemp.set(solar.solarYear, solar.solarMonth-1, solar.solarDay);

//                CalendarQueryHandler.insertEvent(getActivity().getApplicationContext(),
//                        timeLongTemp.getTimeInMillis(), record.getName());
        });



//            CalendarQueryHandler.insertEvent(getActivity().getApplicationContext(),
//                    timeLong.getTimeInMillis(), "some event");
    }

    private void addEvent(String title, String location, long begin, long end) {
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