package com.example.wojciech.iotmonitor.features.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.model.thingspeak.ThingspeakResponse;
import com.example.wojciech.iotmonitor.net.RequestManager;
import com.example.wojciech.iotmonitor.net.VolleyCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<Set<Credentials>> credentialsLive;
    private CredentialsRepository credentialsRepository;
    private MutableLiveData<Boolean> isChannelListEmpty = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<HashMap<String, List<FieldValueListItem>>> expandableListDetailLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        credentialsRepository = CredentialsRepository.getInstance(application.getApplicationContext());
        credentialsLive = credentialsRepository.getCredentials();
        expandableListDetailLiveData = new MutableLiveData<>();
    }

    public void init() {
        expandableListDetailLiveData.postValue(new HashMap<>());
        isLoading.postValue(true);
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

    public void addChannels(List<Credentials> credentials) {
        for (Credentials c : credentials) {
            RequestManager.getInstance().requestFeed(c, getApplication().getApplicationContext(), 1, new VolleyCallback() {
                @Override
                public void onSuccess(ThingspeakResponse thingspeakResponse) {
                    addNewChannel(thingspeakResponse);
                }

                @Override
                public void onError(VolleyError error) {
                    Log.d(TAG, "onSuccess: error " + error);
                }
            });


        }
    }

    public void addNewChannel(ThingspeakResponse thingspeakResponse) {
        List<FieldValueListItem> s = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String field = thingspeakResponse.getChannel().getField(i + 1);
            String value = thingspeakResponse.getFeeds()[0].getField(i + 1);
            s.add(new FieldValueListItem(field, value));
        }
        HashMap<String, List<FieldValueListItem>> value = expandableListDetailLiveData.getValue();
        if (value != null) {
            value.put(thingspeakResponse.getChannel().getName(), s);
            expandableListDetailLiveData.postValue(value);
        }
        if (expandableListDetailLiveData.getValue() != null
                && credentialsLive.getValue() != null
                && expandableListDetailLiveData.getValue().size() == credentialsLive.getValue().size()) {
            isLoading.postValue(false);
        }
    }

    public LiveData<HashMap<String, List<FieldValueListItem>>> getExpandableListDetailLiveData() {
        return expandableListDetailLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }


}
