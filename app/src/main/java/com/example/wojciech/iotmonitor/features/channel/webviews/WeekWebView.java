package com.example.wojciech.iotmonitor.features.channel.webviews;

import android.net.Uri;

import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

public class WeekWebView extends AbstractWebView {

    public WeekWebView(Credentials credentials) {
        super(credentials);
    }

    @Override
    public String getUrl(int fieldNr, int width, int height, float density) {
        Uri.Builder builder = getPrebuiltUrl(fieldNr, width, height, density);
        builder.appendQueryParameter("days", "7")
                .appendQueryParameter("median", "240");

        return builder.build().toString();
    }
}
