package com.example.wojciech.iotmonitor.model.thingspeak;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FieldSettingsDao {

    @Insert
    void insert(FieldSettings fieldSettings);

    @Update
    void update(FieldSettings fieldSettings);

    @Delete
    void delete(FieldSettings fieldSettings);

    @Query("SELECT * FROM field_settings")
    LiveData<List<FieldSettings>> getAllFieldSettings();

    @Query("SELECT * FROM field_settings WHERE channelId=:channelId ORDER BY field ASC")
    LiveData<List<FieldSettings>> getChannelFieldSettingsByChannelId(int channelId);

    @Query("DELETE FROM field_settings WHERE channelId=:channelId")
    void deleteChannelFieldSettingsByChannelId(int channelId);
}
