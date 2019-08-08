package com.example.wojciech.iotmonitor.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.util.Set;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<Set<Credentials>> credentialsLive;
    private CredentialsRepository credentialsRepository;
    private MutableLiveData<Boolean> isChannelListEmpty = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        credentialsRepository = CredentialsRepository.getInstance(application.getApplicationContext());
        credentialsLive = credentialsRepository.getCredentials();
    }

    public LiveData<Set<Credentials>> getCredentials() {
        return credentialsLive;
    }

    public LiveData<Boolean> isChannelListEmpty() {
        return isChannelListEmpty;
    }

    public void credentialsListChanged() {
        Set<Credentials> credentials = credentialsLive.getValue();
        if (credentials == null) {
            isChannelListEmpty.setValue(true);
        }
        isChannelListEmpty.setValue(credentialsLive.getValue().isEmpty());
    }
}
