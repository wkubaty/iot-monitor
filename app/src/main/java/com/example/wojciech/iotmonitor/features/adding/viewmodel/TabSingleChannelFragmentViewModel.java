package com.example.wojciech.iotmonitor.features.adding.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.wojciech.iotmonitor.features.adding.IChannelProvider;

public class TabSingleChannelFragmentViewModel extends ViewModel {

    private static final String TAG = TabSingleChannelFragmentViewModel.class.getSimpleName();
    private IChannelProvider searchChannel;
    public MutableLiveData<Boolean> isPrivateLiveData;

    public void init(IChannelProvider searchChannel) {
        this.searchChannel = searchChannel;
        isPrivateLiveData = new MutableLiveData<>();
    }

    public void getChannelsWithUserApiKey(int channelId, String apiKey) {
        searchChannel.addChannel(channelId, apiKey);
    }

    public void setPrivateChannel(boolean isChecked) {
        isPrivateLiveData.postValue(isChecked);
    }
}
