package com.example.wojciech.iotmonitor;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.model.thingspeak.CredentialsDao;
import com.example.wojciech.iotmonitor.model.thingspeak.IotDatabase;

import java.util.List;

public class CredentialsRepository {

    private CredentialsDao credentialsDao;
    private LiveData<List<Credentials>> credentials;


    public CredentialsRepository(Application application) {
        IotDatabase database = IotDatabase.getInstance(application);
        credentialsDao = database.credentialsDao();
        credentials = credentialsDao.getAllCredentials();
    }

    public void insert(Credentials credentials) {
        new InsertTask(credentialsDao).execute(credentials);
    }

    public void update(Credentials credentials) {
        new UpdateTask(credentialsDao).execute(credentials);
    }

    public void delete(Credentials credentials) {
        new DeleteTask(credentialsDao).execute(credentials);
    }

    public void deleteById(int id) {
        new DeleteByIdTask(credentialsDao).execute(id);
    }


    public LiveData<List<Credentials>> getCredentials() {
        return credentials;
    }

    public void deleteAllCredentials() {
        new DeleteAllTask(credentialsDao).execute();
    }


    private static class InsertTask extends AsyncTask<Credentials, Void, Void> {
        private static final String TAG = InsertTask.class.getSimpleName();
        private CredentialsDao credentialsDao;

        public InsertTask(CredentialsDao credentialsDao) {
            this.credentialsDao = credentialsDao;
        }

        @Override
        protected Void doInBackground(Credentials... credentialsEntities) {
            credentialsDao.insert(credentialsEntities[0]);
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Credentials, Void, Void> {
        private CredentialsDao credentialsDao;

        public UpdateTask(CredentialsDao credentialsDao) {
            this.credentialsDao = credentialsDao;
        }

        @Override
        protected Void doInBackground(Credentials... credentialsEntities) {
            credentialsDao.update(credentialsEntities[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Credentials, Void, Void> {
        private CredentialsDao credentialsDao;

        public DeleteTask(CredentialsDao credentialsDao) {
            this.credentialsDao = credentialsDao;
        }

        @Override
        protected Void doInBackground(Credentials... credentialsEntities) {
            credentialsDao.delete(credentialsEntities[0]);
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {
        private CredentialsDao credentialsDao;

        public DeleteAllTask(CredentialsDao credentialsDao) {
            this.credentialsDao = credentialsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            credentialsDao.deleteAllCredentials();
            return null;
        }
    }

    private static class DeleteByIdTask extends AsyncTask<Integer, Void, Void> {
        private CredentialsDao credentialsDao;

        public DeleteByIdTask(CredentialsDao credentialsDao) {
            this.credentialsDao = credentialsDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            credentialsDao.deleteByChannelId(integers[0]);
            return null;
        }
    }
}
