package com.example.wojciech.iotmonitor;

import android.content.Context;

import com.example.wojciech.iotmonitor.model.thingspeak.ChannelSettings;
import com.example.wojciech.iotmonitor.prefs.SharedPrefsManager;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ChannelSettingsManager {
    private static volatile ChannelSettingsManager uniqueInstance;
    private static Map<Integer, ChannelSettings> settings;
    private static final String SETTINGS_PREFS_KEY = "channel_settings";
    private final SharedPrefsManager sharedPrefsManager;
    private ChannelSettingsManager(Context context) {
        sharedPrefsManager = SharedPrefsManager.getInstance(context);
        Type type = new TypeToken<HashMap<Integer, ChannelSettings>>() {}.getType();
        settings = sharedPrefsManager.getCollection(SETTINGS_PREFS_KEY, type);
        if(settings == null){
            settings = new HashMap<>();
        }
    }

    public static ChannelSettingsManager getInstance(Context context) {
        if (context == null) {
            throw new NullPointerException("Provided application context is null");
        }
        if(uniqueInstance == null) {
            synchronized (ChannelSettingsManager.class){
                if(uniqueInstance == null) {
                    uniqueInstance = new ChannelSettingsManager(context);
                }
            }
        }
        return uniqueInstance;
    }

    public void addSettings(Integer appWidgetId, ChannelSettings channelSettings) {
        settings.put(appWidgetId, channelSettings);
        sharedPrefsManager.setCollection(SETTINGS_PREFS_KEY, settings);
    }

    public ChannelSettings getChannelSettings(int appWidgetId) {
        return settings.get(appWidgetId);
    }

    public void removeChannelSettings(int appWidgetId) {
        settings.remove(appWidgetId);
        sharedPrefsManager.setCollection(SETTINGS_PREFS_KEY, settings);
    }
}
