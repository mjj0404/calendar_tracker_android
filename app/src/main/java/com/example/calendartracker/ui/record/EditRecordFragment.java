package com.example.calendartracker.ui.record;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

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
import android.widget.Toast;

import com.example.calendartracker.R;
import com.example.calendartracker.databinding.FragmentEditRecordBinding;
import com.example.calendartracker.model.Lunar;
import com.example.calendartracker.model.Record;
import com.example.calendartracker.model.Solar;
import com.example.calendartracker.utility.Constants;
import com.example.calendartracker.utility.AlertDialogWithListener;
import com.example.calendartracker.utility.EventConverter;
import com.example.calendartracker.utility.LunarSolarConverter;
import com.example.calendartracker.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditRecordFragment extends Fragment implements TextWatcher{

    private MainViewModel viewModel;
    private FragmentEditRecordBinding binding;
    private boolean isEditing = false;
    
    private static final String TAG = "EditRecord";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = FragmentEditRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel.init();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.editRecordNameEditText.addTextChangedListener(this);
        binding.editRecordLunarDateEditText.addTextChangedListener(this);

        binding.editRecordLeapMonthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isRecordInputValid();
            }
        });

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(R.string.edit_record_add);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.appbar_edit_record, menu);
                if (isEditing) menu.findItem(R.id.menu_item_delete).setVisible(true);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                String actionMenuItem = "ActionMenuItem";
                String recordName = binding.editRecordNameEditText.getText().toString();
                if (menuItem.getItemId() == R.id.menu_item_save && isRecordInputValid()) {
                    int calendarid = (int) LunarSolarConverter.lunarToInt(
                            Objects.requireNonNull(binding.editRecordLunarDateEditText.getUnMaskedText()),
                            binding.editRecordLeapMonthSwitch.isChecked());
                    if (isEditing) {
                        viewModel.updateRecord(getArguments().getInt(Constants.RECORD_ID),
                                binding.editRecordNameEditText.getText().toString(),
                                calendarid);
                    }
                    else {
                        //  if adding new item
                        viewModel.createRecord(binding.editRecordNameEditText.getText().toString(),
                                calendarid);
                        Toast.makeText(requireActivity().getApplicationContext(),
                                getString(R.string.edit_record_user_added, recordName),
                                Toast.LENGTH_SHORT).show();
                    }
                    navigateUp();
                    return true;
                }
                else if (menuItem.getItemId() == R.id.menu_item_delete) {
                    AlertDialogWithListener dialog = new AlertDialogWithListener(
                            requireActivity(),
                            recordName,
                            Constants.DIALOG_DELETE_CONFIRMATION,
                            new AlertDialogWithListener.DialogOnClickListener() {
                                @Override
                                public void onConfirmClick() {
                                    Log.d(TAG, "DeleteConfirmation onConfirmClick: ");
                                    viewModel.deleteRecord(getArguments().getInt(Constants.RECORD_ID));
                                    Toast.makeText(requireActivity().getApplicationContext(),
                                            getString(R.string.edit_record_user_deleted, recordName),
                                            Toast.LENGTH_SHORT).show();
                                    navigateUp();
                                }

                                @Override
                                public void onCancelClick() {
                                    Log.d(TAG, "DeleteConfirmation onCancelClick: ");
                                }
                            });
                    dialog.onCreateDialog(null).show();
                }
                else if (menuItem.toString().contains(actionMenuItem)) {
                    // back arrow menu button pressed
                    navigateUp();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        if (getArguments() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(R.string.edit_record_edit);
            isEditing = true;
            viewModel.getRecord(getArguments().getInt(Constants.RECORD_ID));
            viewModel.getRecordLiveData().observe(getViewLifecycleOwner(), new Observer<Record>() {
                @Override
                public void onChanged(Record record) {
                    Solar solar = LunarSolarConverter.solarFromInt(record.getCalendarid());
                    Lunar lunar = LunarSolarConverter.solarToLunar(solar);

                    binding.editRecordNameEditText.setText(record.getName());
                    binding.editRecordLunarDateEditText.setText(LunarSolarConverter.lunarDateToString(lunar));
                    binding.editRecordLeapMonthSwitch.setChecked(lunar.isleap);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
        String nameString = binding.editRecordNameEditText.getText().toString();
        String dateString = binding.editRecordLunarDateEditText.getUnMaskedText();
        boolean isLeap = binding.editRecordLeapMonthSwitch.isChecked();

        List<Integer> result = inputValidation(nameString, dateString, isLeap);
        if (result.contains(Constants.RECORD_INPUT_VALID)) {
            binding.editRecordInvalidDateWarningTextview.setVisibility(View.INVISIBLE);
            binding.editRecordNameWarningTextview.setVisibility(View.INVISIBLE);
            binding.editRecordCorrespondingGregorianTextview.setText(
                    getCorrespondingGregorianString(dateString, isLeap));
            binding.editRecordGregorianTextview.setVisibility(View.VISIBLE);
            return true;
        }
        if (result.contains(Constants.RECORD_INPUT_EMPTY_NAME))
            binding.editRecordNameWarningTextview.setVisibility(View.VISIBLE);
        if (result.contains(Constants.RECORD_INPUT_INVALID_DATE)) {
            binding.editRecordInvalidDateWarningTextview.setVisibility(View.VISIBLE);
            binding.editRecordInvalidDateWarningTextview.setText(R.string.edit_record_invalid_date);
        }
        binding.editRecordCorrespondingGregorianTextview.setText("");
        binding.editRecordGregorianTextview.setVisibility(View.INVISIBLE);
        return false;
    }

    private void navigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().
                getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController controller = Objects.requireNonNull(navHostFragment).getNavController();
        NavDirections action =
                EditRecordFragmentDirections.actionNavigationEditRecordToNavigationRecords();
        controller.navigate(action);
    }

    private List<Integer> inputValidation (String name, String date, boolean isLeap) {
        List<Integer> result = new ArrayList<>();
        if (name.isEmpty()) result.add(Constants.RECORD_INPUT_EMPTY_NAME);

        if (date.length() == 8) {
            Lunar lunar = Lunar.fromInput(date, isLeap);
            Lunar lunarRecalculated = LunarSolarConverter.solarToLunar(LunarSolarConverter.lunarToSolar(lunar));

            if (!lunar.equals(lunarRecalculated)) {
                result.add(Constants.RECORD_INPUT_INVALID_DATE);
            }
        }
        else {
            result.add(Constants.RECORD_INPUT_INVALID_DATE);
        }
        if (result.isEmpty()) result.add(Constants.RECORD_INPUT_VALID);
        return result;
    }

    private String getCorrespondingGregorianString(String date, boolean isLeap) {
        Lunar lunar = new Lunar(Integer.parseInt(date.substring(0, 4)),
                Integer.parseInt(date.substring(4, 6)),
                Integer.parseInt(date.substring(6)),
                isLeap);
        LunarSolarConverter.lunarToSolar(lunar);
        return EventConverter.fromSolar(LunarSolarConverter.lunarToSolar(lunar))
                .toString().replace(',','/');
    }
}