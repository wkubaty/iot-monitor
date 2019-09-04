package com.example.wojciech.iotmonitor.model.thingspeak;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "field_settings")
public class FieldSettings implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.channelId);
        dest.writeInt(this.field);
        dest.writeByte(this.minTrigger ? (byte) 1 : (byte) 0);
        dest.writeByte(this.maxTrigger ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.minValue);
        dest.writeFloat(this.maxValue);
        dest.writeString(this.bgColor);
    }

    protected FieldSettings(Parcel in) {
        this.id = in.readInt();
        this.channelId = in.readInt();
        this.field = in.readInt();
        this.minTrigger = in.readByte() != 0;
        this.maxTrigger = in.readByte() != 0;
        this.minValue = in.readFloat();
        this.maxValue = in.readFloat();
        this.bgColor = in.readString();
    }

    public static final Creator<FieldSettings> CREATOR = new Creator<FieldSettings>() {
        @Override
        public FieldSettings createFromParcel(Parcel source) {
            return new FieldSettings(source);
        }

        @Override
        public FieldSettings[] newArray(int size) {
            return new FieldSettings[size];
        }
    };
}
