package com.example.calendartracker.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartracker.databinding.FragmentDashboardBinding;
import com.example.calendartracker.model.Record;
import com.example.calendartracker.viewmodel.MainViewModel;

import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private MainViewModel viewModel;
    private EventAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.init();
        adapter = new EventAdapter();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("MAGG", "onCreateView: Dashboard Frag");
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.eventRecyclerview;
        recyclerView.setLayoutManager();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        TextView textView = binding.textDashboard;
//        viewModel.getAllRecordListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Record>>() {
//            @Override
//            public void onChanged(List<Record> records) {
//                Log.d("MAGG", "onChanged: " + records.size());
//                textView.setText(records.get(0).getExternid());
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}