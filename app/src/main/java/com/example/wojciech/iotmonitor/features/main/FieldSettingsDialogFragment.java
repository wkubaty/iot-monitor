package com.example.wojciech.iotmonitor.features.main;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.DialogFieldSettingsBinding;
import com.example.wojciech.iotmonitor.model.thingspeak.FieldSettings;

public class FieldSettingsDialogFragment extends DialogFragment {

    private static final String TAG = FieldSettingsDialogFragment.class.getSimpleName();
    private static final String SETTINGS = "settings";
    private OnYesNoClick onYesNoClick;

    public static FieldSettingsDialogFragment newInstance(FieldSettings settings) {
        FieldSettingsDialogFragment frag = new FieldSettingsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SETTINGS, settings);
        frag.setArguments(args);
        return frag;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FieldSettings fieldSettings = getArguments().getParcelable(SETTINGS);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        FieldSettingsDialogViewModel fieldSettingsDialogViewModel = ViewModelProviders.of(this).get(FieldSettingsDialogViewModel.class);
        DialogFieldSettingsBinding dialogBnd = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_field_settings, null, false);
        dialogBnd.setLifecycleOwner(this);
        fieldSettingsDialogViewModel.init(fieldSettings);
        dialogBnd.setViewModel(fieldSettingsDialogViewModel);
        builder.setTitle("Set triggers");
        builder.setView(dialogBnd.getRoot());

        FieldSettings fieldSettingsTmp = new FieldSettings(fieldSettings.getId(), fieldSettings.getChannelId(), fieldSettings.getField());

        dialogBnd.wConfMinTrigger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fieldSettingsTmp.setMinTrigger(isChecked);
            }
        });

        dialogBnd.wConfMaxTrigger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fieldSettingsTmp.setMaxTrigger(isChecked);
            }
        });

        TextWatcher textWatcherMin = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ifMinValueIsLessOrEqualMaxValue(Float.valueOf(dialogBnd.wConfMinValue.getText().toString()), Float.valueOf(dialogBnd.wConfMaxValue.getText().toString()))) {
                    fieldSettingsTmp.setMinValue(Float.valueOf(s.toString()));

                } else {
                    Toast.makeText(getContext().getApplicationContext(), "Min value can't be higher than max value.", Toast.LENGTH_SHORT).show();
                }

            }
        };

        TextWatcher textWatcherMax = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (ifMinValueIsLessOrEqualMaxValue(Float.valueOf(dialogBnd.wConfMinValue.getText().toString()), Float.valueOf(dialogBnd.wConfMaxValue.getText().toString()))) {
                        fieldSettingsTmp.setMaxValue(Float.valueOf(s.toString()));
                    } else {
                        Toast.makeText(getContext().getApplicationContext(), "Max value can't be lower than min value.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        };
        dialogBnd.wConfMinValue.addTextChangedListener(textWatcherMin);
        dialogBnd.wConfMaxValue.addTextChangedListener(textWatcherMax);
        builder.setPositiveButton("Set", (dialog, which) -> {
            if (onYesNoClick != null) {
                onYesNoClick.onPositiveClicked(fieldSettingsTmp);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", ((dialog, which) -> {
            onYesNoClick.onNegativeClicked();
            dialog.cancel();
        }));

        return builder.create();
    }

    private boolean ifMinValueIsLessOrEqualMaxValue(Float minValue, Float maxValue) {
        try {
            return minValue <= maxValue;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void setOnYesNoClick(OnYesNoClick onYesNoClick) {
        this.onYesNoClick = onYesNoClick;
    }

    public interface OnYesNoClick {
        void onPositiveClicked(FieldSettings fieldSettingsTmp);

        void onNegativeClicked();

    }
}
