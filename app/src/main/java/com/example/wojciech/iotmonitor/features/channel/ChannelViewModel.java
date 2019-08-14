package com.example.wojciech.iotmonitor.features.channel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.wojciech.iotmonitor.features.channel.webviews.AbstractWebView;

import java.util.ArrayList;
import java.util.List;

public class ChannelViewModel extends AndroidViewModel {
    private MutableLiveData<List<String>> urls = new MutableLiveData<>();

    public ChannelViewModel(@NonNull Application application) {
        super(application);

    }

    public LiveData<List<String>> getUrls() {
        return urls;
    }

    public void showWebView(AbstractWebView webView, int width, int height, float density) {
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            urls.add(webView.getUrl(i, width, height, density));
        }
        this.urls.setValue(urls);
    }

}