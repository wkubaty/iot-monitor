package com.example.wojciech.iotmonitor.model.thingspeak.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApiKey implements Parcelable {
    @SerializedName("api_key")
    private String apiKey;

    @SerializedName("write_flag")
    private boolean writeFlag;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.apiKey);
        dest.writeByte(this.writeFlag ? (byte) 1 : (byte) 0);
    }

    public ApiKey() {
    }

    protected ApiKey(Parcel in) {
        this.apiKey = in.readString();
        this.writeFlag = in.readByte() != 0;
    }

    public static final Creator<ApiKey> CREATOR = new Creator<ApiKey>() {
        @Override
        public ApiKey createFromParcel(Parcel source) {
            return new ApiKey(source);
        }

        @Override
        public ApiKey[] newArray(int size) {
            return new ApiKey[size];
        }
    };

    public String getApiKey() {
        return apiKey;
    }


    public boolean isWriteFlag() {
        return writeFlag;
    }
}
