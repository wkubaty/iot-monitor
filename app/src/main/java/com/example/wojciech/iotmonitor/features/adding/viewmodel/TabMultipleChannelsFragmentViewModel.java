package com.example.wojciech.iotmonitor.features.adding.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.wojciech.iotmonitor.features.adding.IChannelProvider;
import com.example.wojciech.iotmonitor.model.thingspeak.info.ChannelInfo;
import com.example.wojciech.iotmonitor.net.AccountInfoVolleyCallback;
import com.example.wojciech.iotmonitor.net.RequestManager;

import java.util.List;

public class TabMultipleChannelsFragmentViewModel extends AndroidViewModel {

    private static final String TAG = TabMultipleChannelsFragmentViewModel.class.getSimpleName();
    private RequestManager manager;
    private IChannelProvider searchChannel;

    public TabMultipleChannelsFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(IChannelProvider searchChannel) {
        this.searchChannel = searchChannel;
        this.manager = RequestManager.getInstance();
    }

    public void getChannelsWithUserApiKey(String apiKey) {
        manager.requestChannelInfo(apiKey, getApplication().getApplicationContext(), new AccountInfoVolleyCallback() {
            @Override
            public void onSuccess(List<ChannelInfo> response) {
                for (ChannelInfo channelInfo : response) {
                    searchChannel.addChannel(channelInfo.getId(), channelInfo.getReadApiKey());
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(getApplication().getApplicationContext(), "There was an error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
