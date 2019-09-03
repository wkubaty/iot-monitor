package com.example.wojciech.iotmonitor;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.wojciech.iotmonitor.model.thingspeak.FieldSettings;
import com.example.wojciech.iotmonitor.model.thingspeak.FieldSettingsDao;
import com.example.wojciech.iotmonitor.model.thingspeak.IotDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FieldSettingsRepository {

    private static final String TAG = FieldSettingsRepository.class.getSimpleName();
    private FieldSettingsDao fieldSettingsDao;
    private LiveData<List<FieldSettings>> fieldSettings;


    public FieldSettingsRepository(Application application) {
        IotDatabase database = IotDatabase.getInstance(application);
        fieldSettingsDao = database.fieldSettingsDao();
        fieldSettings = fieldSettingsDao.getAllFieldSettings();
    }

    public void insert(FieldSettings fieldSettings) {
        new InsertTask(fieldSettingsDao).execute(fieldSettings);
    }

    public void update(FieldSettings fieldSettings) {
        new UpdateTask(fieldSettingsDao).execute(fieldSettings);
    }

    public void delete(FieldSettings fieldSettings) {
        new DeleteTask(fieldSettingsDao).execute(fieldSettings);
    }

    public List<FieldSettings> getChannelFieldSettingsByChannelId(Integer channelId) {
        if (fieldSettings.getValue() == null) {
            return new ArrayList<>();
        }
        return fieldSettings.getValue().stream().filter(f -> f.getChannelId() == channelId).collect(Collectors.toList());
    }

    public void deleteChannelFieldSettingsTask() {
        new DeleteChannelFieldSettingsTask(fieldSettingsDao).execute();
    }

    public FieldSettings getChannelFieldSettingsByChannelIdAndField(int channelId, int field) {
        if (fieldSettings.getValue() == null) {
            return new FieldSettings(channelId, field);
        }
        return fieldSettings.getValue().stream().filter(f -> f.getChannelId() == channelId).filter(f -> f.getField() == field).findFirst().orElse(new FieldSettings(channelId, field));
    }

    public LiveData<List<FieldSettings>> getFieldSettings() {
        return fieldSettings;
    }

    private static class InsertTask extends AsyncTask<FieldSettings, Void, Void> {
        private static final String TAG = InsertTask.class.getSimpleName();
        private FieldSettingsDao fieldSettingsDao;

        public InsertTask(FieldSettingsDao fieldSettingsDao) {
            this.fieldSettingsDao = fieldSettingsDao;
        }

        @Override
        protected Void doInBackground(FieldSettings... fieldSettings) {
            fieldSettingsDao.insert(fieldSettings[0]);
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<FieldSettings, Void, Void> {
        private FieldSettingsDao fieldSettingsDao;

        public UpdateTask(FieldSettingsDao fieldSettingsDao) {
            this.fieldSettingsDao = fieldSettingsDao;
        }

        @Override
        protected Void doInBackground(FieldSettings... fieldSettings) {
            fieldSettingsDao.update(fieldSettings[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<FieldSettings, Void, Void> {
        private FieldSettingsDao fieldSettingsDao;

        public DeleteTask(FieldSettingsDao fieldSettingsDao) {
            this.fieldSettingsDao = fieldSettingsDao;
        }

        @Override
        protected Void doInBackground(FieldSettings... fieldSettings) {
            fieldSettingsDao.delete(fieldSettings[0]);
            return null;
        }
    }

    private static class DeleteChannelFieldSettingsTask extends AsyncTask<Integer, Void, Void> {
        private FieldSettingsDao fieldSettingsDao;

        public DeleteChannelFieldSettingsTask(FieldSettingsDao fieldSettingsDao) {
            this.fieldSettingsDao = fieldSettingsDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            fieldSettingsDao.deleteChannelFieldSettingsByChannelId(integers[0]);
            return null;
        }
    }
}
