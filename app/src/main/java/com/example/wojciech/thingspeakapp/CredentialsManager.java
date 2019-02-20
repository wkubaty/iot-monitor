package com.example.wojciech.thingspeakapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wojciech.thingspeakapp.model.Credentials;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.wojciech.thingspeakapp.MainActivity.channels_prefs;

public class CredentialsManager {
    private static final CredentialsManager credentialsManager = new CredentialsManager();
    private static ArrayList<Credentials> credentials = new ArrayList<>();

    public static CredentialsManager getInstance() {
        return credentialsManager;
    }

    private CredentialsManager() {
    }

    private void saveCredentials(Context context, ArrayList<Credentials> credentials){
        saveArrayList(context, credentials, channels_prefs, "credentials");
    }

    private void saveArrayList(Context context, ArrayList<?> list, String prefs_name, String key){
        SharedPreferences prefs = context.getSharedPreferences(prefs_name, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    private ArrayList<Credentials> loadCredentials(Context context){
        ArrayList<?> credentials;
        credentials = getArrayList(context, channels_prefs, "credentials");
        if(!credentials.isEmpty()){
            return (ArrayList<Credentials>) credentials;
        } else {
            return new ArrayList<>();
        }
    }

    private ArrayList<?> getArrayList(Context context, String prefs_name, String key){
        SharedPreferences prefs = context.getSharedPreferences(prefs_name, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Credentials>>() {}.getType();
        if(gson.fromJson(json, type)==null){
            return new ArrayList<>();
        }
        else {
            return gson.fromJson(json, type);
        }
    }

    public void addCredentials(Context context, Credentials creds) {
        credentials.add(creds);
        saveCredentials(context, credentials);
    }

    public ArrayList<Credentials> getCredentials(Context context) {
        if(credentials == null){
            return loadCredentials(context);
        }
        return credentials;
    }
}
