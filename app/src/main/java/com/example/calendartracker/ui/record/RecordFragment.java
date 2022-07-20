package com.example.calendartracker.ui.record;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.calendartracker.R;
import com.example.calendartracker.databinding.FragmentRecordBinding;
import com.example.calendartracker.model.Record;
import com.example.calendartracker.utility.AlertDialogWithListener;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecordFragment extends Fragment {

    private FragmentRecordBinding binding;
    private MainViewModel viewModel;
    private List<Record> recordList = new ArrayList<>();
    private RecordAdapter adapter;

    private static final String TAG = "RecordFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainViewModel viewModel =
                new ViewModelProvider(this).get(MainViewModel.class);
        Log.d("MAGG", "onCreateView: Home Frag");

        binding = FragmentRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter = new RecordAdapter(new RecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Record record) {
                navigateToEditRecord(record.getRecordid());
            }
        });
        RecyclerView recyclerView = binding.recordRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel.init();

        viewModel.getRecordListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                adapter.setRecordList(records);
                recordList = records;
            }
        });

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.appbar_records, menu);
                MenuItem searchItem = menu.findItem(R.id.menu_item_search);
                SearchView searchView = (SearchView) searchItem.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapter.getFilter().filter(s);
                        return false;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_add) {
                    navigateToEditRecord(-1);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Record record = recordList.get(position);
                    AlertDialogWithListener dialog = new AlertDialogWithListener(
                            requireActivity(),
                            record.getName(),
                            Constants.DIALOG_DELETE_CONFIRMATION,
                            new AlertDialogWithListener.DialogOnClickListener() {
                                @Override
                                public void onConfirmClick() {
                                    viewModel.deleteRecord(record.getRecordid());
                                    adapter.removeAt(position);
                                    Toast.makeText(requireActivity().getApplicationContext(),
                                            getString(R.string.edit_record_user_deleted, record.getName()),
                                            Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelClick() { }
                            });
                    dialog.onCreateDialog(null).show();
                }
                adapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(recyclerView);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void navigateToEditRecord(int recordid) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().
                getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment_activity_main);

        NavController controller = Objects.requireNonNull(navHostFragment).getNavController();

        if (recordid == -1) {
            // no bundle to pass on with navigation
            controller.navigate(R.id.action_navigation_records_to_navigation_edit_record);
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.RECORD_ID, recordid);
            controller.navigate(R.id.action_navigation_records_to_navigation_edit_record, bundle);
        }
    }
}