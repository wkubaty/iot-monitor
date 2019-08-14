package com.example.wojciech.iotmonitor.features.channel.webviews;

import android.net.Uri;

import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

public abstract class AbstractWebView {
    private Uri.Builder builder;
    private Credentials credentials;

    public AbstractWebView(Credentials credentials) {
        this.builder = new Uri.Builder();
        this.credentials = credentials;
    }

    public abstract String getUrl(int fieldNr, int width, int height, float density);

    public Uri.Builder getPrebuiltUrl(int fieldNr, int width, int height, float density) {
        builder.scheme("https")
                .authority("api.thingspeak.com")
                .appendPath("channels")
                .appendPath(String.valueOf(credentials.getId()))
                .appendPath("charts")
                .appendPath(String.valueOf(fieldNr))
                .appendQueryParameter("api_key", credentials.getApiKey())
                .appendQueryParameter("height", String.valueOf((int) (height / density)))
                .appendQueryParameter("width", String.valueOf((int) (width / density)))
                .appendQueryParameter("type", "spline");
        return builder;
    }
}
