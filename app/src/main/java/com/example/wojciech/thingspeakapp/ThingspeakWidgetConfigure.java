package com.example.wojciech.thingspeakapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.example.wojciech.thingspeakapp.databinding.ThingspeakWidgetConfigureBinding;
import com.example.wojciech.thingspeakapp.model.ChannelSettings;
import com.example.wojciech.thingspeakapp.model.Credentials;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ThingspeakWidgetConfigure extends AppCompatActivity {
    static final String CHANNEL_PREFS = "CHANNEL_PREFS";
    static final String PREFS_NAME = "com.example.wojciech.thingspeakapp.ThingspeakWidget";
    static final String PREF_PREFIX_KEY = "appwidget_";
    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private static Map<Integer, ChannelSettings> settings = new HashMap<>(); // appWidgetId->settings
    private ChannelSettings channelSettingsTmp = new ChannelSettings();
    private static ArrayList<Credentials> credentialsList;
    private ThingspeakWidgetConfigureBinding bnd;
    public ThingspeakWidgetConfigure() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = DataBindingUtil.setContentView(this, R.layout.thingspeak_widget_configure);

        setSupportActionBar(bnd.wConfToolbar);

        setResult(RESULT_CANCELED);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        configure(appWidgetId);
    }

    private void configure(int appWidgetId) {
        ChannelSettings channelSettings = loadChannelSettings(this, appWidgetId);
        if(channelSettings == null){
            channelSettings = new ChannelSettings();
        }
        settings.put(appWidgetId, channelSettings);
        bnd.setChannelSettings(channelSettings);
        credentialsList = CredentialsManager.getInstance().getCredentials(this);

        final List<String> names = credentialsList.stream()
                .map(Credentials::getName)
                .collect(Collectors.toList());
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(ThingspeakWidgetConfigure.this, R.layout.channel_list_item, names);
        bnd.wConfSelectChannelSpinner.setAdapter(namesAdapter);
        if(channelSettings.getCredentials() != null){
            //todo id
            bnd.wConfSelectChannelSpinner.setSelection(names.indexOf(channelSettings.getCredentials().getName()));
        }

        bnd.wConfSelectChannelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                Credentials credentials = credentialsList.get(position);
                channelSettingsTmp.setCredentials(credentials);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<Integer> fieldsAdapter = new ArrayAdapter<>(ThingspeakWidgetConfigure.this, R.layout.channel_list_item, Arrays.asList(1,2,3,4,5,6,7,8));
        bnd.wConfSelectFieldSpinner.setAdapter(fieldsAdapter);


        bnd.wConfSelectFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                channelSettingsTmp.setFieldNr(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {
            channelSettingsTmp.setMinTrigger(bnd.wConfMinTrigger.isChecked());
            channelSettingsTmp.setMaxTrigger(bnd.wConfMaxTrigger.isChecked());
        };


        bnd.wConfMinTrigger.setOnCheckedChangeListener(onCheckedChangeListener);
        bnd.wConfMaxTrigger.setOnCheckedChangeListener(onCheckedChangeListener);
        TextWatcher textWatcherMin = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                channelSettingsTmp.setMinValue(Float.valueOf(bnd.wConfMinValue.getText().toString()));
            }
        };
        TextWatcher textWatcherMax = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                channelSettingsTmp.setMaxValue(Float.valueOf(bnd.wConfMaxValue.getText().toString()));
            }
        };

        bnd.wConfMinValue.addTextChangedListener(textWatcherMin);
        bnd.wConfMaxValue.addTextChangedListener(textWatcherMax);

        ViewTreeObserver viewTree = bnd.wConfRefreshTimeSeekbar.getViewTreeObserver();
        viewTree.addOnPreDrawListener(() -> {
            int finalWidth = bnd.wConfRefreshTimeSeekbar.getMeasuredWidth();
            int val = (bnd.wConfRefreshTimeSeekbar.getProgress() * (finalWidth - 2 * bnd.wConfRefreshTimeSeekbar.getThumbOffset()-50)) / bnd.wConfRefreshTimeSeekbar.getMax();
            int setx = (int) bnd.wConfRefreshTimeSeekbar.getX() + val + bnd.wConfRefreshTimeSeekbar.getThumbOffset()-20;

            bnd.wConfRefreshTimeValue.setX(setx);

            return true;
        });
        bnd.wConfRefreshTimeValue.setText(String.format("%smin", String.valueOf(channelSettings.getRefreshTime())));

        bnd.wConfRefreshTimeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                channelSettingsTmp.setRefreshTime(progress+1);
                //TODO calibrate
                int valx = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset()-50)) / seekBar.getMax();
                int setxx = (int) seekBar.getX() + valx + seekBar.getThumbOffset()-20;
                bnd.wConfRefreshTimeValue.setX(setxx);
                bnd.wConfRefreshTimeValue.setText(String.format("%smin", channelSettingsTmp.getRefreshTime()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.thingspeak_widget_configure_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.widget_config_menu_cancel:
                finish();
                return true;
            case R.id.widget_config_menu_confirm:
                settings.put(appWidgetId, channelSettingsTmp);
                saveChannelSettings(ThingspeakWidgetConfigure.this, appWidgetId);
                Context context = ThingspeakWidgetConfigure.this;
                ThingspeakWidget.updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void saveChannelSettings(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences("thingspeak_widget_prefs_" + appWidgetId, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(settings.get(appWidgetId));
        editor.putString("channel_settings", json);
        editor.apply();
    }

    static ChannelSettings loadChannelSettings(Context context, int mAppWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences("thingspeak_widget_prefs_" + mAppWidgetId, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("channel_settings", null);
        if(json == null){
            return null;
        }
        return gson.fromJson(json, ChannelSettings.class);
    }

    static void deleteChannelSettings(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }



}
