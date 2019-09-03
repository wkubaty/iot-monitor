package com.example.wojciech.iotmonitor.features.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.wojciech.iotmonitor.model.thingspeak.FieldSettings;

public class FieldSettingsDialogViewModel extends AndroidViewModel {
    private MutableLiveData<FieldSettings> fieldSettingsLive;

    public FieldSettingsDialogViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(FieldSettings fieldSettings) {
        fieldSettingsLive = new MutableLiveData<>();
        fieldSettingsLive.postValue(fieldSettings);
    }

    public LiveData<FieldSettings> getFieldSettingsLive() {
        return fieldSettingsLive;
    }

}
