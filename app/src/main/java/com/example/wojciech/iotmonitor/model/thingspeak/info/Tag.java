package com.example.wojciech.iotmonitor.model.thingspeak.info;

import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {
    private int id;
    private String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public Tag() {
    }

    protected Tag(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
}
