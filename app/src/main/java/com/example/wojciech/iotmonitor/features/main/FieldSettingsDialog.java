package com.example.wojciech.iotmonitor.features.main;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.wojciech.iotmonitor.databinding.DialogFieldSettingsBinding;
import com.example.wojciech.iotmonitor.model.thingspeak.FieldSettings;

public class FieldSettingsDialog {

    private static final String TAG = FieldSettingsDialog.class.getSimpleName();
    private Context context;
    private DialogFieldSettingsBinding dialogBnd;
    private MainViewModel viewModel;

    public FieldSettingsDialog(Context context, DialogFieldSettingsBinding dialogFieldSettingsBinding, MainViewModel mainViewModel) {
        this.context = context;
        this.dialogBnd = dialogFieldSettingsBinding;
        this.viewModel = mainViewModel;
    }

    public void display(FieldSettings fieldSettings1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Set triggers");
        builder.setView(dialogBnd.getRoot());

        FieldSettings fieldSettingsTmp = new FieldSettings(fieldSettings1.getId(), fieldSettings1.getChannelId(), fieldSettings1.getField());

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
                if (ifMinValueIsLessOrEqualMaxValue()) {
                    fieldSettingsTmp.setMinValue(Float.valueOf(s.toString()));

                } else {
                    Toast.makeText(context.getApplicationContext(), "Min value can't be higher than max value.", Toast.LENGTH_SHORT).show();
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
                    if (ifMinValueIsLessOrEqualMaxValue()) {
                        fieldSettingsTmp.setMaxValue(Float.valueOf(s.toString()));
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Max value can't be lower than min value.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        };
        dialogBnd.wConfMinValue.addTextChangedListener(textWatcherMin);
        dialogBnd.wConfMaxValue.addTextChangedListener(textWatcherMax);
        builder.setPositiveButton("Set", (dialog, which) -> {
            viewModel.updateFieldSetting(fieldSettingsTmp);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", ((dialog, which) -> {
            dialog.cancel();
        }));
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private boolean ifMinValueIsLessOrEqualMaxValue() {
        return true;
        //fixme
//        try {
//            return Float.valueOf(bnd.wConfMinValue.getText().toString()) <= Float.valueOf(bnd.wConfMaxValue.getText().toString());
//        } catch (NumberFormatException e) {
//            return true;
//        }
    }
}
