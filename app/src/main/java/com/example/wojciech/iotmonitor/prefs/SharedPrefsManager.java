package com.example.wojciech.iotmonitor.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class SharedPrefsManager {
    private static final String TAG = SharedPrefsManager.class.getName();
    private static final String PREFS_NAME = "iotmonitor";
    private static SharedPrefsManager uniqueInstance;
    private SharedPreferences prefs;

    private SharedPrefsManager(Context appContext) {
        prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefsManager getInstance(Context context) {
        if (context == null) {
            throw new NullPointerException("Provided application context is null");
        }
        if (uniqueInstance == null) {
            synchronized (SharedPrefsManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new SharedPrefsManager(context);
                }
            }
        }
        return uniqueInstance;

    }

    private static String createJSONStringFromObject(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public <C> void setCollection(String key, C dataCollection) {
        SharedPreferences.Editor editor = prefs.edit();
        String value = createJSONStringFromObject(dataCollection);
        editor.putString(key, value);
        editor.apply();
    }

    public <C> C getCollection(String key, Type typeOfC) {
        String jsonData = prefs.getString(key, null);
        if (null != jsonData) {
            try {
                Gson gson = new Gson();
                C arrFromPrefs = gson.fromJson(jsonData, typeOfC);
                return arrFromPrefs;
            } catch (ClassCastException cce) {
                Log.d(TAG, "Cannot convert string obtained from prefs into collection of type " + typeOfC.toString() + "\n" + cce.getMessage());
            }
        }
        return null;
    }
}

