package com.bitjam.calendartracker.ui.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitjam.calendartracker.R;
import com.bitjam.calendartracker.databinding.FragmentEventBinding;
import com.bitjam.calendartracker.model.Record;
import com.bitjam.calendartracker.model.Solar;
import com.bitjam.calendartracker.utility.AlertDialogWithListener;
import com.bitjam.calendartracker.utility.CalendarQueryHandler;
import com.bitjam.calendartracker.utility.Constants;
import com.bitjam.calendartracker.utility.LunarSolarConverter;
import com.bitjam.calendartracker.utility.PreferenceManager;
import com.bitjam.calendartracker.viewmodel.MainViewModel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
                if (isPermissionGranted(result.values())) {
                    AlertDialogWithListener dialog = new AlertDialogWithListener(
                        requireActivity(),
                        Constants.DIALOG_CREATE_EVENT,
                        new AlertDialogWithListener.DialogOnClickListener() {
                            @Override
                            public void onConfirmClick() {
                                viewModel.queryUpcomingEventList(upcomingEventList, new CalendarQueryHandler.OnQueryFinishListener() {
                                    @Override
                                    public void onQueryFinished(String name) {
                                        Toast.makeText(requireActivity(),
                                                requireActivity().getString(R.string.parse_finished, name),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onCancelClick() {
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
                            public void onConfirmClick() { }

                            @Override
                            public void onCancelClick() { }
                        }
                    );
                    dialog.onCreateDialog(null).show();
                }
                else if (!isPermissionGranted(result.values())) {
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

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Record record = upcomingEventList.get(position);
                    if (!PreferenceManager.getInstance().isSingleItemParsingAllowed()) {
                        AlertDialogWithListener dialog = new AlertDialogWithListener(
                                requireActivity(),
                                Constants.DIALOG_PARSE_SINGLE_RECORD,
                                new AlertDialogWithListener.DialogOnClickListener() {
                                    @Override
                                    public void onConfirmClick() {
                                        PreferenceManager.getInstance().setSingleItemParsingAllowed(true);
                                        addEvent(record);
                                    }

                                    @Override
                                    public void onCancelClick() { }
                                }
                        );
                        dialog.onCreateDialog(null).show();
                    }
                    else {
                        addEvent(record);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addEvent(Record record) {
        Solar solar = LunarSolarConverter.solarFromInt(record.getCalendarid());
        LocalDate date = LocalDate.of(solar.solarYear, solar.solarMonth, solar.solarDay);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, record.getName())
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private boolean isPermissionGranted(Collection<Boolean> results) {
        int count = (int) results.stream().filter(aBoolean -> aBoolean).count();
        return count == results.size();
    }
}