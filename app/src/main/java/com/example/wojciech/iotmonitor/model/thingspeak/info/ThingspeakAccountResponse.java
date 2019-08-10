package com.example.wojciech.iotmonitor.model.thingspeak.info;

import android.os.Parcel;
import android.os.Parcelable;


public class ThingspeakAccountResponse implements Parcelable {
    private ChannelInfo[] channelInfo;

    public ThingspeakAccountResponse(ChannelInfo[] channelInfo) {
        this.channelInfo = channelInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.channelInfo, flags);
    }

    protected ThingspeakAccountResponse(Parcel in) {
        this.channelInfo = in.createTypedArray(ChannelInfo.CREATOR);
    }

    public static final Creator<ThingspeakAccountResponse> CREATOR = new Creator<ThingspeakAccountResponse>() {
        @Override
        public ThingspeakAccountResponse createFromParcel(Parcel source) {
            return new ThingspeakAccountResponse(source);
        }

        @Override
        public ThingspeakAccountResponse[] newArray(int size) {
            return new ThingspeakAccountResponse[size];
        }
    };
}
