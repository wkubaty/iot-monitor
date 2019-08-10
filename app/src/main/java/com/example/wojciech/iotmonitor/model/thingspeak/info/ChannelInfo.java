package com.example.wojciech.iotmonitor.model.thingspeak.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChannelInfo implements Parcelable {
    private int id;
    private String name;
    private String description;
    private String latitude;
    private String longitude;
    @SerializedName("created_at")
    private String createdAt;
    private String elevation;
    @SerializedName("last_entry_id")
    private int lastEntryId;
    @SerializedName("public_flag")
    private boolean publicFlag;
    private String url;
    private int ranking;
    private String metadata;
    @SerializedName("license_id")
    private String licenseId;
    @SerializedName("github_url")
    private String githubUrl;
    private Tag[] tags;
    @SerializedName("api_keys")
    private ApiKey[] apiKeys;

    public ChannelInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.createdAt);
        dest.writeString(this.elevation);
        dest.writeInt(this.lastEntryId);
        dest.writeByte(this.publicFlag ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);
        dest.writeInt(this.ranking);
        dest.writeString(this.metadata);
        dest.writeString(this.licenseId);
        dest.writeString(this.githubUrl);
        dest.writeTypedArray(this.tags, flags);
        dest.writeTypedArray(this.apiKeys, flags);
    }

    protected ChannelInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.createdAt = in.readString();
        this.elevation = in.readString();
        this.lastEntryId = in.readInt();
        this.publicFlag = in.readByte() != 0;
        this.url = in.readString();
        this.ranking = in.readInt();
        this.metadata = in.readString();
        this.licenseId = in.readString();
        this.githubUrl = in.readString();
        this.tags = in.createTypedArray(Tag.CREATOR);
        this.apiKeys = in.createTypedArray(ApiKey.CREATOR);
    }

    public static final Creator<ChannelInfo> CREATOR = new Creator<ChannelInfo>() {
        @Override
        public ChannelInfo createFromParcel(Parcel source) {
            return new ChannelInfo(source);
        }

        @Override
        public ChannelInfo[] newArray(int size) {
            return new ChannelInfo[size];
        }
    };
}
