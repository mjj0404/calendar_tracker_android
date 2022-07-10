package com.example.calendartracker.ui.record;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
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

public class RecordFragment extends Fragment {

    private FragmentRecordBinding binding;
    private MainViewModel viewModel;
    private RecordAdapter adapter;

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
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().
                        getSupportFragmentManager().
                        findFragmentById(R.id.nav_host_fragment_activity_main);

                NavController controller = navHostFragment.getNavController();

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



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}