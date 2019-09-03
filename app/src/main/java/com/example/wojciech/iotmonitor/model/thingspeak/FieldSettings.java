package com.example.wojciech.iotmonitor.model.thingspeak;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "field_settings")
public class FieldSettings {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int channelId;
    private int field;
    private boolean minTrigger;
    private boolean maxTrigger;
    private float minValue;
    private float maxValue;
    private String bgColor;

    public FieldSettings(int channelId, int field) {
        this.channelId = channelId;
        this.field = field;
        this.minTrigger = false;
        this.maxTrigger = false;
        this.minValue = 0.0f;
        this.maxValue = 0.0f;
        this.bgColor = "#000000";
    }

    @Ignore
    public FieldSettings(int id, int channelId, int field) {
        this.id = id;
        this.channelId = channelId;
        this.field = field;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public int getChannelId() {
        return channelId;
    }

    public int getField() {
        return field;
    }

    @Override
    public String toString() {
        return "FieldSettings{" +
                "minTrigger=" + minTrigger +
                ", maxTrigger=" + maxTrigger +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", bgColor='" + bgColor + '\'' +
                '}';
    }
}
