package com.example.wojciech.iotmonitor.features.adding.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.FieldSettingsRepository;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.model.thingspeak.FieldSettings;

public class AddChannelViewModel extends AndroidViewModel {
    private static final String TAG = AddChannelViewModel.class.getSimpleName();
    private CredentialsRepository credentialsRepository;
    private FieldSettingsRepository fieldSettingsRepository;


    public AddChannelViewModel(@NonNull Application application) {
        super(application);
        credentialsRepository = new CredentialsRepository(application);
        fieldSettingsRepository = new FieldSettingsRepository(application);
    }

    public void addCredentials(Credentials credentials) {
        credentialsRepository.insert(credentials);
    }

    public void addFieldSettings(FieldSettings fieldSettings) {
        Log.d(TAG, "addFieldSettings: inserting fieldsettings " + fieldSettings);
        fieldSettingsRepository.insert(fieldSettings);
        Log.d(TAG, "addFieldSettings: inserting fieldsettings livedataavalue " + fieldSettingsRepository.getFieldSettings().getValue());

    }
}
