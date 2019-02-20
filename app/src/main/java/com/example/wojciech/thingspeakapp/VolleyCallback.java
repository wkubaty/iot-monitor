package com.example.wojciech.thingspeakapp;

import com.android.volley.VolleyError;
import com.example.wojciech.thingspeakapp.model.ThingspeakResponse;

public interface VolleyCallback{
    void onSuccess(ThingspeakResponse thingspeakResponse);

    void onError(VolleyError error);
}