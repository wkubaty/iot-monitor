package com.example.wojciech.iotmonitor.features.channel.webviews;

import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

public class WebViewFactory {

    public static AbstractWebView getWebView(int position, Credentials credentials) {
        switch (position) {
            case 0:
                return new HourWebView(credentials);
            case 1:
                return new DayWebView(credentials);
            case 2:
                return new WeekWebView(credentials);
            case 3:
                return new MonthWebView(credentials);
            default:
                return new HourWebView(credentials);
        }
    }
}
