package com.example.calendartracker.ui.record;

import androidx.core.view.MenuProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.calendartracker.R;
import com.example.calendartracker.databinding.FragmentEditRecordBinding;
import com.example.calendartracker.model.Lunar;
import com.example.calendartracker.model.Record;
import com.example.calendartracker.model.Solar;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.utility.LunarSolarConverter;
import com.example.calendartracker.viewmodel.MainViewModel;

import java.util.List;

public class EditRecordFragment extends Fragment implements TextWatcher{

    private MainViewModel viewModel;
    private FragmentEditRecordBinding binding;
    private boolean isEditing = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = FragmentEditRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel.init();


        if (getArguments() != null) {
            isEditing = true;
            viewModel.getRecord(getArguments().getInt(Constants.RECORD_ID));
            viewModel.getRecordLiveData().observe(getViewLifecycleOwner(), new Observer<Record>() {
                @Override
                public void onChanged(Record record) {
                    Solar solar = LunarSolarConverter.SolarFromInt(record.getCalendarid());
                    Lunar lunar = LunarSolarConverter.SolarToLunar(solar);

                    binding.editRecordNameEditText.setText(record.getName());
                    binding.editRecordLunarDateEditText.setText(lunar.toString());
                    binding.editRecordLeapMonthSwitch.setChecked(lunar.isleap);
                }
            });
        }

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.appbar_save_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_save && isRecordInputValid()) {
                    Log.d("MAGG", "onMenuItemSelected: ");
                    if (isEditing) {
                        // TODO: editing
                    }
                    else {
                        // TODO: adding new
                    }
                }
                return false;
            }
        });

        binding.editRecordNameEditText.addTextChangedListener(this);
        binding.editRecordLunarDateEditText.addTextChangedListener(this);

        binding.editRecordLeapMonthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isRecordInputValid();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("MAGG", "onDestroyView: ");
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void afterTextChanged(Editable editable) {
        isRecordInputValid();
    }

    private boolean isRecordInputValid() {
        List<Integer> result = viewModel.inputValidation(
                binding.editRecordNameEditText.getText().toString(),
                binding.editRecordLunarDateEditText.getUnMaskedText(),
                binding.editRecordLeapMonthSwitch.isChecked()
        );
        if (result.contains(Constants.RECORD_INPUT_VALID)) {
            binding.editRecordInvalidDateWarningTextview.setVisibility(View.INVISIBLE);
            binding.editRecordNameWarningTextview.setVisibility(View.INVISIBLE);
            return true;
        }
        if (result.contains(Constants.RECORD_INPUT_EMPTY_NAME))
            binding.editRecordNameWarningTextview.setVisibility(View.VISIBLE);
        if (result.contains(Constants.RECORD_INPUT_INVALID_DATE)) {
            binding.editRecordInvalidDateWarningTextview.setVisibility(View.VISIBLE);
            binding.editRecordInvalidDateWarningTextview.setText(R.string.edit_record_invalid_date);
        }
        return false;
    }
}