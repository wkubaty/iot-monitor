package com.example.wojciech.iotmonitor.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.wojciech.iotmonitor.ChannelSettingsManager;
import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.WidgetConfigureBinding;
import com.example.wojciech.iotmonitor.model.thingspeak.ChannelSettings;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WidgetConfigureActivity extends AppCompatActivity {
    private static final String TAG = "WidgetConfigureActivity";
    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ChannelSettingsManager channelSettingsManager;
    CredentialsRepository credentialsRepository;
    private ChannelSettings channelSettingsTmp = new ChannelSettings();
    private WidgetConfigureBinding bnd;

    public WidgetConfigureActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        channelSettingsManager = ChannelSettingsManager.getInstance(this);
        credentialsRepository = CredentialsRepository.getInstance(this);

        bnd = DataBindingUtil.setContentView(this, R.layout.widget_configure);

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
        ChannelSettingsManager.getInstance(this);
        CredentialsRepository.getInstance(this);
        ChannelSettings channelSettings = channelSettingsManager.getChannelSettings(appWidgetId);
        if (channelSettings == null) {
            //todo return?
            channelSettings = new ChannelSettings();
        }
        bnd.setChannelSettings(channelSettings);
        Set<Credentials> credentialsList = credentialsRepository.getCredentials().getValue();

        final List<String> names = credentialsList.stream()
                .map(Credentials::getName)
                .collect(Collectors.toList());
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(WidgetConfigureActivity.this, R.layout.widget_spinner_item, names);
        bnd.wConfChannelsSpinner.setAdapter(namesAdapter);
        bnd.wConfChannelsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                channelSettingsTmp.setCredentials(credentialsList.stream().filter(c -> c.getName().equals(namesAdapter.getItem(position))).findFirst().get());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        ArrayAdapter<Integer> fieldsAdapter = new ArrayAdapter<>(WidgetConfigureActivity.this, R.layout.widget_spinner_item, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

        bnd.wConfFieldsSpinner.setAdapter(fieldsAdapter);
        bnd.wConfFieldsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                channelSettingsTmp.setFieldNr(position + 1);

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
                if (ifMinValueIsLessOrEqualMaxValue()) {
                    channelSettingsTmp.setMinValue(Float.valueOf(bnd.wConfMinValue.getText().toString()));
                } else {
                    Toast.makeText(getApplicationContext(), "Min value can't be higher than max value.", Toast.LENGTH_SHORT).show();
                }

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

                try {
                    if (ifMinValueIsLessOrEqualMaxValue()) {
                        channelSettingsTmp.setMaxValue(Float.valueOf(bnd.wConfMaxValue.getText().toString()));
                    } else {
                        Toast.makeText(getApplicationContext(), "Max value can't be lower than min value.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        };

        bnd.wConfMinValue.addTextChangedListener(textWatcherMin);
        bnd.wConfMaxValue.addTextChangedListener(textWatcherMax);

        bnd.wConfRefreshTimeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                channelSettingsTmp.setRefreshTime(progress + 1);
                bnd.wConfRefreshTimeValue.setText(String.format("%s min", channelSettingsTmp.getRefreshTime()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private boolean ifMinValueIsLessOrEqualMaxValue() {
        return Float.valueOf(bnd.wConfMinValue.getText().toString()) <= Float.valueOf(bnd.wConfMaxValue.getText().toString());
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
                ChannelSettingsManager.getInstance(this).addSettings(appWidgetId, channelSettingsTmp);
                Context context = WidgetConfigureActivity.this;
                Widget.initializeAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId);

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

}
