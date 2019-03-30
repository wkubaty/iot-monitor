package com.example.wojciech.iotmonitor;

import android.content.Context;

import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.prefs.SharedPrefsManager;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;

public class CredentialsManager {

    private static CredentialsManager uniqueInstance;
    private HashSet<Credentials> credentials;
    private SharedPrefsManager sharedPrefsManager;
    private static final String CREDENTIALS_PREFS_KEY = "credentials";

    private CredentialsManager(Context context) {
        SharedPrefsManager.initialize(context);
        sharedPrefsManager = SharedPrefsManager.getInstance();
        Type type = new TypeToken<HashSet<Credentials>>() {}.getType();
        credentials = sharedPrefsManager.getCollection(CREDENTIALS_PREFS_KEY, type);
        if (credentials==null){
            credentials = new HashSet<>();
        }
    }

    public static CredentialsManager getInstance() {
        if (uniqueInstance == null) {
            throw new IllegalStateException(
                    "CredentialsManager is not initialized, call initialize(applicationContext) " +
                            "static method first");
        }
        return uniqueInstance;
    }

    public static void initialize(Context context) {
        if (context == null) {
            throw new NullPointerException("Provided application context is null");
        }
        if (uniqueInstance == null) {
            synchronized (CredentialsManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new CredentialsManager(context);
                }
            }
        }
    }

    public void addCredentials(Credentials creds) {
        credentials.add(creds);
        sharedPrefsManager.setCollection(CREDENTIALS_PREFS_KEY, credentials);
    }

    public HashSet<Credentials> getCredentials() {
        return credentials;
    }

    public void removeCredentials(int id) {
        credentials.removeIf(creds -> creds.getId() == id);
        sharedPrefsManager.setCollection(CREDENTIALS_PREFS_KEY, credentials);
    }

}
