package com.example.wojciech.iotmonitor.features.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.FieldSettingsRepository;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.model.thingspeak.FieldSettings;
import com.example.wojciech.iotmonitor.model.thingspeak.ThingspeakResponse;
import com.example.wojciech.iotmonitor.net.RequestManager;
import com.example.wojciech.iotmonitor.net.VolleyCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
    private static final int INTERVAL = 60000; // in ms
    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ssX";
    private LiveData<List<Credentials>> credentialsLive;
    private LiveData<List<FieldSettings>> fieldSettingsLive;
    private CredentialsRepository credentialsRepository;
    private FieldSettingsRepository fieldSettingsRepository;
    private MutableLiveData<Boolean> isChannelListEmpty = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<HashMap<ChannelStatus, List<FieldValueListItem>>> expandableListDetailLiveData;
    private Handler handler;
    private Runnable statusChecker = new Runnable() {
        @Override
        public void run() {
            HashMap<ChannelStatus, List<FieldValueListItem>> expandableListDetailLiveDataValue = expandableListDetailLiveData.getValue();
            HashMap<ChannelStatus, List<FieldValueListItem>> expandableListDetailLiveDataValue2 = new HashMap<>();
            if (expandableListDetailLiveDataValue != null) {
                expandableListDetailLiveDataValue.forEach((k, value) -> k.setOrUpdate());
                expandableListDetailLiveData.postValue(expandableListDetailLiveDataValue);
                handler.postDelayed(statusChecker, INTERVAL);
            }

        }
    };

    public MainViewModel(@NonNull Application application) {
        super(application);
        credentialsRepository = new CredentialsRepository(application);
        fieldSettingsRepository = new FieldSettingsRepository(getApplication());
        credentialsLive = credentialsRepository.getCredentials();
        fieldSettingsLive = fieldSettingsRepository.getFieldSettings();
        expandableListDetailLiveData = new MutableLiveData<HashMap<ChannelStatus, List<FieldValueListItem>>>();
    }

    public void init() {
        expandableListDetailLiveData.postValue(new HashMap<ChannelStatus, List<FieldValueListItem>>());
        isLoading.postValue(true);
        handler = new Handler();
    }

    public void clear() {
        handler.removeCallbacks(statusChecker);
    }

    public LiveData<List<Credentials>> getCredentialsLive() {
        return credentialsLive;
    }

    public LiveData<List<FieldSettings>> getFieldSettingsLive() {
        return fieldSettingsLive;
    }

    public LiveData<Boolean> isChannelListEmpty() {
        return isChannelListEmpty;
    }

    public void credentialsListChanged() {
        List<Credentials> credentials = credentialsLive.getValue();
        if (credentials == null) {
            isChannelListEmpty.setValue(true);
        }
        isChannelListEmpty.setValue(credentialsLive.getValue().isEmpty());
    }

    public void fetchChannelsData(List<Credentials> credentials) {
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
        List<FieldValueListItem> fieldValueListItems = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String field = thingspeakResponse.getChannel().getField(i + 1);
            String value = thingspeakResponse.getFeeds()[0].getField(i + 1);
            fieldValueListItems.add(new FieldValueListItem(field, value));
        }
        HashMap<ChannelStatus, List<FieldValueListItem>> value = expandableListDetailLiveData.getValue();
        if (value != null) {

            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN, Locale.ENGLISH);
            try {
                String channelName = thingspeakResponse.getChannel().getName();
                Date lastUpdate = sdf.parse(thingspeakResponse.getFeeds()[0].getCreatedAt());
                value.put(new ChannelStatus(channelName, lastUpdate), fieldValueListItems);
                expandableListDetailLiveData.postValue(value);
                handler.removeCallbacks(statusChecker);
                statusChecker.run();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (expandableListDetailLiveData.getValue() != null
                && credentialsLive.getValue() != null
                && expandableListDetailLiveData.getValue().size() == credentialsLive.getValue().size()) {
            isLoading.postValue(false);
        }
    }

    public LiveData<HashMap<ChannelStatus, List<FieldValueListItem>>> getExpandableListDetailLiveData() {
        return expandableListDetailLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public List<FieldSettings> getChannelFieldSettingsByChannelId(int channelId) {
        return fieldSettingsRepository.getChannelFieldSettingsByChannelId(channelId);
    }

    public void updateFieldSetting(FieldSettings fieldSettings) {
        fieldSettingsRepository.update(fieldSettings);
    }

    public void deleteByChannelId(int channelId) {
        credentialsRepository.deleteById(channelId);
    }

    public FieldSettings getChannelFieldSettingsByChannelIdAndField(int channelId, int field) {
        return fieldSettingsRepository.getChannelFieldSettingsByChannelIdAndField(channelId, field);
    }
}
