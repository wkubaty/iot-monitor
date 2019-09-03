package com.example.wojciech.iotmonitor.model.thingspeak;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Credentials.class, FieldSettings.class}, version = 5)
public abstract class IotDatabase extends RoomDatabase {
    private static final String DB_NAME = "iot_database";
    private static IotDatabase instance;

    public static synchronized IotDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), IotDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract CredentialsDao credentialsDao();

    public abstract FieldSettingsDao fieldSettingsDao();
}
