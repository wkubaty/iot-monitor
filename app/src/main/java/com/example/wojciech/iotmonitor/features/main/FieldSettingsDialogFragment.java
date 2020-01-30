package com.example.wojciech.iotmonitor.features.main;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
    public static final String DIALOGBUTTON = "onDialogButtonClick";
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
        if (getArguments().getParcelable("onDialogButtonClick") != null) {
            onDialogButtonClick = getArguments().getParcelable(DIALOGBUTTON);
        }

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

        builder.setPositiveButton(getString(R.string.field_settings_positive_button), (dialog, which) -> {

        });
        builder.setNegativeButton(getString(R.string.field_settings_negative_button), ((dialog, which) -> {
            onDialogButtonClick.onNegativeClicked();
            builder.setCancelable(true);

            dialog.cancel();
        }));

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button confirmSettingsButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                confirmSettingsButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (onDialogButtonClick != null) {
                            if (dialogBnd.wConfMinTrigger.isChecked() && dialogBnd.wConfMaxTrigger.isChecked()) {
                                Float minValue = Float.valueOf(dialogBnd.wConfMinValue.getText().toString());
                                Float maxValue = Float.valueOf(dialogBnd.wConfMaxValue.getText().toString());
                                if (ifMinValueIsLessOrEqualMaxValue(minValue, maxValue)) {
                                    onDialogButtonClick.onPositiveClicked(fieldSettingsDialogViewModel.getFieldSettingsTmpLive().getValue());
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(getContext().getApplicationContext(), getString(R.string.field_settings_min_max_warning), Toast.LENGTH_SHORT).show();
                                }
                            } else if (dialogBnd.wConfMinTrigger.isChecked() || dialogBnd.wConfMaxTrigger.isChecked()) {
                                onDialogButtonClick.onPositiveClicked(fieldSettingsDialogViewModel.getFieldSettingsTmpLive().getValue());
                                alertDialog.dismiss();
                            } else {
                                alertDialog.dismiss();
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
        if (getArguments() != null) {
            getArguments().putParcelable(DIALOGBUTTON, onDialogButtonClick);
        }
    }

    public interface OnDialogButtonClick extends Parcelable {
        void onPositiveClicked(FieldSettings fieldSettingsTmp);

        void onNegativeClicked();

        @Override
        int describeContents();

        @Override
        void writeToParcel(Parcel dest, int flags);
    }
}
