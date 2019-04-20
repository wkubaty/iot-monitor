package com.example.wojciech.iotmonitor.model.thingspeak;

public class ChannelSettings {
    private Credentials credentials;
    private int fieldNr;
    private int refreshTime;
    private boolean minTrigger;
    private boolean maxTrigger;
    private float minValue;
    private float maxValue;

    public ChannelSettings() {
        this.fieldNr = 1;
        this.refreshTime = 60;
        this.minTrigger = false;
        this.maxTrigger = false;
        this.minValue = 0.0f;
        this.maxValue = 0.0f;
    }

    public ChannelSettings(Credentials credentials, int fieldNr, int refreshTime) {
        this();
        this.credentials = credentials;
        this.fieldNr = fieldNr;
        this.refreshTime = refreshTime;
    }

    public ChannelSettings(ChannelSettings channelSettings) {
        this.credentials = channelSettings.credentials;
        this.fieldNr = channelSettings.fieldNr;
        this.refreshTime = channelSettings.refreshTime;
        this.minTrigger = channelSettings.minTrigger;
        this.maxTrigger = channelSettings.maxTrigger;
        this.minValue = channelSettings.minValue;
        this.maxValue = channelSettings.maxValue;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public int getFieldNr() {
        return fieldNr;
    }

    public void setFieldNr(int fieldNr) {
        this.fieldNr = fieldNr;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public boolean isMinTrigger() {
        return minTrigger;
    }

    public void setMinTrigger(boolean minTrigger) {
        this.minTrigger = minTrigger;
    }

    public boolean isMaxTrigger() {
        return maxTrigger;
    }

    public void setMaxTrigger(boolean maxTrigger) {
        this.maxTrigger = maxTrigger;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public String toString() {
        return "ChannelSettings{" +
                "credentials=" + credentials +
                ", fieldNr=" + fieldNr +
                ", refreshTime=" + refreshTime +
                ", minTrigger=" + minTrigger +
                ", maxTrigger=" + maxTrigger +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                '}';
    }
}
