package com.example.wojciech.iotmonitor.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

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

    public void showWebView(Credentials credentials, String start, int width, int height, float density) {
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            urls.add(getUrl(credentials, start, i, width, height, density));
        }
        this.urls.setValue(urls);
    }


    public String getUrl(Credentials credentials, String start, int fieldNr, int width, int height, float density) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.thingspeak.com")
                .appendPath("channels")
                .appendPath(String.valueOf(credentials.getId()))
                .appendPath("charts")
                .appendPath(String.valueOf(fieldNr))
                .appendQueryParameter("api_key", credentials.getApiKey())
                .appendQueryParameter("start", start)
                .appendQueryParameter("offset", "+02.0")
                .appendQueryParameter("height", String.valueOf((int) (height / density)))
                .appendQueryParameter("width", String.valueOf((int) (width / density)))
                .appendQueryParameter("type", "spline")
                .appendQueryParameter("color", "FF3300")
                .appendQueryParameter("bgcolor", "FFFFFF");
        return builder.build().toString();
    }


}