package com.example.calendartracker.ui.record;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calendartracker.R;
import com.example.calendartracker.databinding.FragmentEditRecordBinding;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.viewmodel.MainViewModel;

public class EditRecordFragment extends Fragment {

    private MainViewModel viewModel;
    private FragmentEditRecordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = FragmentEditRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel.init();

//        getArguments().getInt(Constants.RECORD_ID)

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}