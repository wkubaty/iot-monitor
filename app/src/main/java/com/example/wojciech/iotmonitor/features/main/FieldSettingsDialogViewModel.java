package com.example.wojciech.iotmonitor.features.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.wojciech.iotmonitor.model.thingspeak.FieldSettings;

public class FieldSettingsDialogViewModel extends AndroidViewModel {
    private MutableLiveData<FieldSettings> fieldSettingsLive;
    private MutableLiveData<FieldSettings> fieldSettingsTmpLive;
    private MutableLiveData<Boolean> minTrigger;
    private MutableLiveData<Boolean> maxTrigger;

    public FieldSettingsDialogViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(FieldSettings fieldSettings) {
        fieldSettingsLive = new MutableLiveData<>();
        fieldSettingsLive.postValue(fieldSettings);

        fieldSettingsTmpLive = new MutableLiveData<>();
        FieldSettings fieldSettingsTmp = null;
        try {
            fieldSettingsTmp = (FieldSettings) fieldSettings.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        fieldSettingsTmpLive.setValue(fieldSettingsTmp);
        minTrigger = new MutableLiveData<>();
        minTrigger.postValue(fieldSettingsTmp.isMinTrigger());
        maxTrigger = new MutableLiveData<>();
        maxTrigger.postValue(fieldSettingsTmp.isMaxTrigger());

    }

    public LiveData<FieldSettings> getFieldSettingsLive() {
        return fieldSettingsLive;
    }

    public LiveData<FieldSettings> getFieldSettingsTmpLive() {
        return fieldSettingsTmpLive;
    }

    public MutableLiveData<Boolean> getMinTrigger() {
        return minTrigger;
    }

    public MutableLiveData<Boolean> getMaxTrigger() {
        return maxTrigger;
    }

    public void setTmpMinTrigger(boolean isChecked) {
        FieldSettings value = fieldSettingsTmpLive.getValue();
        value.setMinTrigger(isChecked);
        fieldSettingsTmpLive.postValue(value);
    }

    public void setTmpMaxTrigger(boolean isChecked) {
        FieldSettings value = fieldSettingsTmpLive.getValue();
        value.setMaxTrigger(isChecked);
        fieldSettingsTmpLive.postValue(value);
    }
}
