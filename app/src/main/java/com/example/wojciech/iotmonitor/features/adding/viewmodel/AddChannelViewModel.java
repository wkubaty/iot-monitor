package com.example.wojciech.iotmonitor.features.adding.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

public class AddChannelViewModel extends AndroidViewModel {
    private CredentialsRepository credentialsRepository;


    public AddChannelViewModel(@NonNull Application application) {
        super(application);
        credentialsRepository = CredentialsRepository.getInstance(application.getApplicationContext());
    }

    public void addCredentials(Credentials credentials) {
        credentialsRepository.addCredentials(credentials);
    }
}
