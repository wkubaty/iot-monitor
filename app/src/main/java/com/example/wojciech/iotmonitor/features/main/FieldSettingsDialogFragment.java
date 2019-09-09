package com.example.wojciech.iotmonitor.features.main;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.DialogFieldSettingsBinding;
import com.example.wojciech.iotmonitor.model.thingspeak.FieldSettings;

public class FieldSettingsDialogFragment extends DialogFragment {

    private static final String TAG = FieldSettingsDialogFragment.class.getSimpleName();
    private static final String SETTINGS = "settings";
    private OnDialogButtonClick onDialogButtonClick;

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
        builder.setTitle("Field settings");


        dialogBnd.wConfMinTrigger.setOnCheckedChangeListener((buttonView, isChecked) -> {
            fieldSettingsDialogViewModel.setTmpMinTrigger(isChecked);
        });

        dialogBnd.wConfMaxTrigger.setOnCheckedChangeListener((buttonView, isChecked) -> {
            fieldSettingsDialogViewModel.setTmpMaxTrigger(isChecked);
        });

        dialogBnd.setViewModel(fieldSettingsDialogViewModel);
        builder.setView(dialogBnd.getRoot());

        builder.setPositiveButton("Set", (dialog, which) -> {

        });
        builder.setNegativeButton("Cancel", ((dialog, which) -> {
            onDialogButtonClick.onNegativeClicked();
            builder.setCancelable(true);

            dialog.cancel();
        }));

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        if (onDialogButtonClick != null) {
                            if (ifMinValueIsLessOrEqualMaxValue(Float.valueOf(dialogBnd.wConfMinValue.getText().toString()), Float.valueOf(dialogBnd.wConfMaxValue.getText().toString()))) {
                                onDialogButtonClick.onPositiveClicked(fieldSettingsDialogViewModel.getFieldSettingsTmpLive().getValue());

                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(getContext().getApplicationContext(), "Max value can't be lower than min value.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        });
        return alertDialog;

    }

    private boolean ifMinValueIsLessOrEqualMaxValue(Float minValue, Float maxValue) {
        try {
            return minValue <= maxValue;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void setOnDialogButtonClick(OnDialogButtonClick onDialogButtonClick) {
        this.onDialogButtonClick = onDialogButtonClick;
    }

    public interface OnDialogButtonClick {
        void onPositiveClicked(FieldSettings fieldSettingsTmp);

        void onNegativeClicked();

    }
}
