package com.example.calendartracker.ui.event;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartracker.databinding.FragmentEventBinding;
import com.example.calendartracker.databinding.FragmentEventBinding;
import com.example.calendartracker.model.Record;
import com.example.calendartracker.viewmodel.MainViewModel;

import java.util.List;

public class EventFragment extends Fragment {

    private FragmentEventBinding binding;
    private MainViewModel viewModel;
    private EventAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("MAGG", "onCreateView: Dashboard Frag");
        binding = FragmentEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter = new EventAdapter();
        RecyclerView recyclerView = binding.eventRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.init();

//        viewModel.getAllRecordListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Record>>() {
//            @Override
//            public void onChanged(List<Record> records) {
//                adapter.setRecordList(records);
//            }
//        });
        viewModel.getUpcomingEventListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                adapter.setRecordList(records);
            }
        });
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