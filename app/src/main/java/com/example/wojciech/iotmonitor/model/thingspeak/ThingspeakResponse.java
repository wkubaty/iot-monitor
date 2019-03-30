package com.example.wojciech.iotmonitor.model.thingspeak;

import java.io.Serializable;
import java.util.Arrays;

public class ThingspeakResponse implements Serializable {
    private Channel channel;
    private Feed[] feeds;

    @Override
    public String toString() {
        return "ThingspeakResponse{" +
                "channel=" + channel +
                ", feeds=" + Arrays.toString(feeds) +
                '}';
    }

    public Channel getChannel() {
        return channel;
    }

    public Feed[] getFeeds() {
        return feeds;
    }
}
