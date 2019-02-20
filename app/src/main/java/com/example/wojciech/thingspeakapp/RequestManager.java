package com.example.wojciech.thingspeakapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wojciech.thingspeakapp.model.Credentials;
import com.example.wojciech.thingspeakapp.model.ThingspeakResponse;
import com.google.gson.Gson;

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

}
