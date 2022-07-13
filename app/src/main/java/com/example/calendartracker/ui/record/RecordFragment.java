package com.example.calendartracker.ui.record;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.calendartracker.R;
import com.example.calendartracker.databinding.FragmentRecordBinding;
import com.example.calendartracker.model.Record;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.viewmodel.MainViewModel;

import java.util.List;
import java.util.Objects;

public class RecordFragment extends Fragment {

    private FragmentRecordBinding binding;
    private MainViewModel viewModel;
    private RecordAdapter adapter;

    private static final String TAG = "Record";

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
                NavHostFragment navHostFragment = (NavHostFragment) requireActivity().
                        getSupportFragmentManager().
                        findFragmentById(R.id.nav_host_fragment_activity_main);

                NavController controller = Objects.requireNonNull(navHostFragment).getNavController();

                NavDirections action =
                        RecordFragmentDirections.actionNavigationRecordsToNavigationEditRecord();

                Bundle bundle = new Bundle();
                bundle.putInt(Constants.RECORD_ID, record.getRecordid());
                controller.navigate(R.id.action_navigation_records_to_navigation_edit_record, bundle);

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
                        Log.d(TAG, "onQueryTextSubmit: " + s);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d(TAG, "onQueryTextChange: " + s);
                        adapter.getFilter().filter(s);
                        return false;
                    }
                });

                searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        return false;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        return false;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_add) {
                    NavHostFragment navHostFragment = (NavHostFragment) requireActivity().
                            getSupportFragmentManager().
                            findFragmentById(R.id.nav_host_fragment_activity_main);

                    NavController controller = Objects.requireNonNull(navHostFragment).getNavController();
                    NavDirections actions = RecordFragmentDirections.
                            actionNavigationRecordsToNavigationEditRecord();
                    Log.d(TAG, "onMenuItemSelected: ");

                    controller.navigate(actions);
                    return true;
                }

                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
//        requireActivity().addMenuProvider(new MenuProvider() {
//            @Override
//            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
//                menu.clear();
//            }
//
//            @Override
//            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
//                return false;
//            }
//        });
    }
}