package com.example.wojciech.thingspeakapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

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

    public ThingspeakWidgetConfigure() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thingspeak_widget_configure);
        Toolbar toolbar = findViewById(R.id.thingspeak_widget_configure_toolbar);
        CheckBox minTrigger = findViewById(R.id.thingspeak_widget_configure_select_channel_min_trigger);
        CheckBox maxTrigger = findViewById(R.id.thingspeak_widget_configure_select_channel_max_trigger);
        EditText minValue = findViewById(R.id.thingspeak_widget_configure_select_channel_min_value);
        EditText maxValue = findViewById(R.id.thingspeak_widget_configure_select_channel_max_value);
        Spinner channelSpinner = findViewById(R.id.select_channel_spinner);
        Spinner fieldSpinner = findViewById(R.id.select_field_spinner);
        SeekBar refreshTimeSeekbar = findViewById(R.id.refresh_time_seekbar);

        setSupportActionBar(toolbar);

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        credentialsList = CredentialsManager.getInstance().getCredentials(this);

        final List<String> names = credentialsList.stream()
                .map(Credentials::getName)
                .collect(Collectors.toList());
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(ThingspeakWidgetConfigure.this, R.layout.channel_list_item, names);
        channelSpinner.setAdapter(namesAdapter);

        channelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                ChannelSettings channelSettings = settings.get(appWidgetId);
                if(channelSettings == null){
                    Credentials credentials = credentialsList.get(position);
                    channelSettingsTmp.setCredentials(credentials);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<Integer> fieldsAdapter = new ArrayAdapter<>(ThingspeakWidgetConfigure.this, R.layout.channel_list_item, Arrays.asList(1,2,3,4,5,6,7,8));

        fieldSpinner.setAdapter(fieldsAdapter);
        fieldSpinner.setSelection(0);

        fieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                channelSettingsTmp.setFieldNr(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {
            channelSettingsTmp.setMinTrigger(minTrigger.isChecked());
            channelSettingsTmp.setMaxTrigger(maxTrigger.isChecked());
        };

        minTrigger.setOnCheckedChangeListener(onCheckedChangeListener);
        maxTrigger.setOnCheckedChangeListener(onCheckedChangeListener);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                channelSettingsTmp.setMinValue(Float.valueOf(minValue.getText().toString()));
                channelSettingsTmp.setMaxValue(Float.valueOf(maxValue.getText().toString()));
            }
        };

        minValue.setText("0.0");
        maxValue.setText("0.0");
        minValue.addTextChangedListener(textWatcher);
        maxValue.addTextChangedListener(textWatcher);
        refreshTimeSeekbar.setMax(59);

        TextView refreshTimeValueTV = findViewById(R.id.refresh_time_value);

        refreshTimeSeekbar.setProgress(0);
        int val = (refreshTimeSeekbar.getProgress() * (refreshTimeSeekbar.getWidth() - 2 * refreshTimeSeekbar.getThumbOffset()-50)) / refreshTimeSeekbar.getMax();
        int setx = (int) refreshTimeSeekbar.getX() + val + refreshTimeSeekbar.getThumbOffset()-20;
        channelSettingsTmp.setRefreshTime(1);
        channelSettingsTmp.setFieldNr(1);
        refreshTimeValueTV.setX(setx);
        refreshTimeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                channelSettingsTmp.setRefreshTime(progress+1);
                //TODO calibrate
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset()-50)) / seekBar.getMax();
                int setx = (int) seekBar.getX() + val + seekBar.getThumbOffset()-20;
                refreshTimeValueTV.setX(setx);
                refreshTimeValueTV.setText(String.format("%smin", String.valueOf(progress+1)));
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
        String json = gson.toJson(channelSettingsTmp);
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
