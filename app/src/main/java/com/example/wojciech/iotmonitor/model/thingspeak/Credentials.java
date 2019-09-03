package com.example.wojciech.iotmonitor.model.thingspeak;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "credentials")
public class Credentials implements Parcelable {

    public static final Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel source) {
            return new Credentials(source);
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };
    @PrimaryKey
    private int id;
    private String name;
    private String apiKey;

    @Ignore
    public Credentials(int id, String apiKey) {
        this.id = id;
        this.name = "";
        this.apiKey = apiKey;
    }

    public Credentials(int id, String name, String apiKey) {
        this.id = id;
        this.name = name;
        this.apiKey = apiKey;
    }

    @Ignore
    protected Credentials(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.apiKey = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.apiKey);
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
