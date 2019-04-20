package com.example.wojciech.iotmonitor.net;

import com.android.volley.VolleyError;
import com.example.wojciech.iotmonitor.model.thingspeak.ThingspeakResponse;

public interface VolleyCallback {
    void onSuccess(ThingspeakResponse thingspeakResponse);

    void onError(VolleyError error);
}