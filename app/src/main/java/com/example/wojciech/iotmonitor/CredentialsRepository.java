package com.example.wojciech.iotmonitor;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.prefs.SharedPrefsManager;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class CredentialsRepository {

    private static final String CREDENTIALS_PREFS_KEY = "credentials";
    private static CredentialsRepository uniqueInstance;
    private MutableLiveData<Set<Credentials>> credentialsLive;
    private SharedPrefsManager sharedPrefsManager;

    private CredentialsRepository(Context context) {
        sharedPrefsManager = SharedPrefsManager.getInstance(context);
        Type type = new TypeToken<HashSet<Credentials>>() {
        }.getType();
        Set<Credentials> credentials = sharedPrefsManager.getCollection(CREDENTIALS_PREFS_KEY, type);

        if (credentials == null) {
            credentials = new HashSet<>();
        }
        credentialsLive = new MutableLiveData<>();
        credentialsLive.setValue(credentials);
    }

    public static CredentialsRepository getInstance(Context context) {
        if (context == null) {
            throw new NullPointerException("Provided application context is null");
        }
        if (uniqueInstance == null) {
            synchronized (CredentialsRepository.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new CredentialsRepository(context);
                }
            }
        }
        return uniqueInstance;
    }

    public void addCredentials(Credentials creds) {
        Set<Credentials> credentials = credentialsLive.getValue();
        removeCredentials(creds.getId());
        credentials.add(creds);
        sharedPrefsManager.setCollection(CREDENTIALS_PREFS_KEY, credentials);
        credentialsLive.setValue(credentials);
    }

    public LiveData<Set<Credentials>> getCredentials() {
        return credentialsLive;
    }

    public void removeCredentials(int id) {
        Set<Credentials> credentials = credentialsLive.getValue();
        Credentials credentialsToRemove = credentials.stream()
                .filter(c -> c.getId() == id)
                .findFirst().orElse(null);
        if (credentialsToRemove != null) {
            credentials.remove(credentialsToRemove);
            sharedPrefsManager.setCollection(CREDENTIALS_PREFS_KEY, credentials);
        }
        credentialsLive.setValue(credentials);
    }


}
