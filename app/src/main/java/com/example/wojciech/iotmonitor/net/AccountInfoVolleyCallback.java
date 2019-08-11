package com.example.wojciech.iotmonitor.net;

import com.android.volley.VolleyError;
import com.example.wojciech.iotmonitor.model.thingspeak.info.ChannelInfo;

import java.util.List;

public interface AccountInfoVolleyCallback {
    void onSuccess(List<ChannelInfo> response);

    void onError(VolleyError error);
}