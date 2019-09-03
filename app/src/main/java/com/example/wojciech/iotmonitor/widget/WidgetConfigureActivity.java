package com.example.wojciech.iotmonitor.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SeekBar;

import com.example.wojciech.iotmonitor.Color;
import com.example.wojciech.iotmonitor.ColorAdapter;
import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.WidgetSettingsManager;
import com.example.wojciech.iotmonitor.databinding.WidgetConfigureBinding;
import com.example.wojciech.iotmonitor.model.thingspeak.ChannelSettings;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WidgetConfigureActivity extends AppCompatActivity {
    private static final String TAG = "WidgetConfigureActivity";
    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    WidgetSettingsManager widgetSettingsManager;
    CredentialsRepository credentialsRepository;
    private ChannelSettings channelSettingsTmp;
    private WidgetConfigureBinding bnd;

    public WidgetConfigureActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        widgetSettingsManager = WidgetSettingsManager.getInstance(this);
        credentialsRepository = new CredentialsRepository(getApplication());
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
        channelSettingsTmp = widgetSettingsManager.getChannelSettings(appWidgetId);
        if (channelSettingsTmp == null) {
            channelSettingsTmp = new ChannelSettings();
        }
        configure(appWidgetId);
    }

    private void configure(int appWidgetId) {
        Log.d(TAG, "configure: with appwid: " + appWidgetId);
        WidgetSettingsManager.getInstance(this);
//        CredentialsRepository.getInstance(this);
        ChannelSettings channelSettings = widgetSettingsManager.getChannelSettings(appWidgetId);
        if (channelSettings == null) {
            //todo return?
            channelSettings = new ChannelSettings();
        }
//        bnd.setChannelSettings(channelSettings);
        List<Credentials> value = credentialsRepository.getCredentials().getValue();

        final List<String> names;
        if (value == null) {
            return;
        }
        names = value.stream()
                .map(Credentials::getName)
                .collect(Collectors.toList());
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(WidgetConfigureActivity.this, R.layout.widget_spinner_item, names);
        bnd.wConfChannelsSpinner.setAdapter(namesAdapter);
        if (channelSettings.getCredentials() != null) {
            bnd.wConfChannelsSpinner.setSelection(names.indexOf(channelSettings.getCredentials().getName()));
        }
        bnd.wConfChannelsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                channelSettingsTmp.setCredentials(value.stream().filter(c -> c.getName().equals(namesAdapter.getItem(position))).findFirst().get());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        ArrayAdapter<Integer> fieldsAdapter = new ArrayAdapter<>(WidgetConfigureActivity.this, R.layout.widget_spinner_item, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

        bnd.wConfFieldsSpinner.setAdapter(fieldsAdapter);

        bnd.wConfFieldsSpinner.setSelection(channelSettingsTmp.getFieldNr() - 1);
        bnd.wConfFieldsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                channelSettingsTmp.setFieldNr(position + 1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bnd.setColorButton.setBackground(new ColorDrawable(android.graphics.Color.parseColor(channelSettingsTmp.getBgColor())));

//        bnd.wConfMinValue.setText(String.format(Locale.ENGLISH, "%.1f", channelSettings.getMinValue()));
//        bnd.wConfMaxValue.setText(String.format(Locale.ENGLISH, "%.1f", channelSettings.getMaxValue()));
//        bnd.wConfMinValue.addTextChangedListener(textWatcherMin);
//        bnd.wConfMaxValue.addTextChangedListener(textWatcherMax);

        bnd.wConfRefreshTimeValue.setText(String.format("%s min", channelSettings.getRefreshTime()));
        bnd.wConfRefreshTimeSeekbar.setProgress(channelSettings.getRefreshTime());
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
                WidgetSettingsManager.getInstance(this).addSettings(appWidgetId, channelSettingsTmp);
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

    public void setColor(View view) {
        AlertDialog dialog = new colorAlertDialog(this);
        dialog.show();
    }

    private class colorAlertDialog extends AlertDialog {

        protected colorAlertDialog(Context context) {
            super(context);

            String[] colors = context.getResources().getStringArray(R.array.colors);
            ArrayList<Color> cols = new ArrayList<>();
            Arrays.stream(colors).forEach(c -> cols.add(new Color(c)));
            ColorAdapter colorAdapter = new ColorAdapter(context, cols);

            setTitle("Choose a color");
            final View customLayout = getLayoutInflater().inflate(R.layout.color_list, null);
            GridView gridView = customLayout.findViewById(R.id.color_list_view);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "onItemClick: position: " + position);
                    channelSettingsTmp.setBgColor(cols.get(position).getValue());
                    bnd.setColorButton.setBackground(new ColorDrawable(android.graphics.Color.parseColor(channelSettingsTmp.getBgColor())));
                    dismiss();
                }
            });
            gridView.setAdapter(colorAdapter);
            setView(customLayout);

        }

    }
}
