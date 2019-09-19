package com.example.wojciech.iotmonitor.model.thingspeak;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CredentialsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Credentials credentials);

    @Update
    void update(Credentials credentials);

    @Delete
    void delete(Credentials credentials);

    @Query("DELETE FROM credentials")
    void deleteAllCredentials();

    @Query("SELECT * FROM Credentials ORDER BY name ASC")
    LiveData<List<Credentials>> getAllCredentials();

    @Query("DELETE FROM Credentials WHERE id = :channelId")
    void deleteByChannelId(int channelId);
}
