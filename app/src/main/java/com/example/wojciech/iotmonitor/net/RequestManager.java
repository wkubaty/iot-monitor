package com.example.wojciech.iotmonitor.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.model.thingspeak.ThingspeakResponse;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RequestManager {
    private static final String THINGSPEAK_URL = "https://api.thingspeak.com";
    private static RequestManager requestManager;

    private RequestManager() {}

    public static RequestManager getInstance(){
        if(requestManager == null){
            requestManager = new RequestManager();
        }
        return requestManager;
    }

    public void requestFeed(Credentials credentials, final Context context, int results, final VolleyCallback callback) {
        int channelId = credentials.getId();
        String apiKey = credentials.getApiKey();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = String.format("%s/channels/%s/feeds.json?api_key=%s&results=%s", THINGSPEAK_URL, channelId, apiKey, results);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                rawResponse -> {
                    Gson gson = new Gson();
                    ThingspeakResponse thingspeakResponse = gson.fromJson(rawResponse, ThingspeakResponse.class);
                    callback.onSuccess(thingspeakResponse);
                }, error -> {
                    callback.onError(error);
                });

        queue.add(stringRequest);
    }
    public void requestFeedFromTo(Credentials credentials, final Context context, Date start, Date end, final VolleyCallback callback) {
        int channelId = credentials.getId();
        String apiKey = credentials.getApiKey();
        RequestQueue queue = Volley.newRequestQueue(context);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String parsedStart = sdf.format(start);
        String parsedEnd = sdf.format(end);
        String url = String.format("%s/channels/%s/feeds.json?api_key=%s&start=%s&end=%s", THINGSPEAK_URL, channelId, apiKey, parsedStart, parsedEnd);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                rawResponse -> {
                    Gson gson = new Gson();
                    ThingspeakResponse thingspeakResponse = gson.fromJson(rawResponse, ThingspeakResponse.class);
                    callback.onSuccess(thingspeakResponse);
                }, error -> {
            callback.onError(error);
        });

        queue.add(stringRequest);
    }

}
