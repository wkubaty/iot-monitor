package com.example.wojciech.iotmonitor.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.android.volley.VolleyError;
import com.example.wojciech.iotmonitor.ChannelSettingsManager;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.activities.ChannelActivity;
import com.example.wojciech.iotmonitor.model.thingspeak.ChannelSettings;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.model.thingspeak.ThingspeakResponse;
import com.example.wojciech.iotmonitor.net.RequestManager;
import com.example.wojciech.iotmonitor.net.VolleyCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Widget extends AppWidgetProvider {

    public static void initializeAppWidget(Context context, AppWidgetManager appWidgetManager,
                                           int appWidgetId) {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        setValueButtonOnClick(context, appWidgetId, views);

        setSettingsButtonOnClick(context, appWidgetId, views);

        setRefreshButtonOnClick(context, appWidgetId, views);

        setAlarm(context, appWidgetId);
        updateWidget(context, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    private static void setAlarm(Context context, int appWidgetId) {
        Alarm alarm = new Alarm();
        alarm.cancelAlarm(context);
        ChannelSettingsManager channelSettingsManager = ChannelSettingsManager.getInstance(context);

        int refreshTime = channelSettingsManager.getChannelSettings(appWidgetId).getRefreshTime();
        alarm.setAlarm(context, appWidgetId, refreshTime);

    }

    private static void setValueButtonOnClick(Context context, int appWidgetId, RemoteViews views) {
        Intent openChannelIntent = new Intent(context, ChannelActivity.class);
        ChannelSettingsManager channelSettingsManager = ChannelSettingsManager.getInstance(context);

        ChannelSettings channelSettings = channelSettingsManager.getChannelSettings(appWidgetId);
        openChannelIntent.putExtra("credentials", channelSettings.getCredentials());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, openChannelIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_value_button, pendingIntent);
    }

    private static void setSettingsButtonOnClick(Context context, int appWidgetId, RemoteViews views) {
        Intent widgetConfigureIntent = new Intent(context, WidgetConfigureActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        widgetConfigureIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent widgetConfigurePendingIntent = PendingIntent.getActivity(context, 30, widgetConfigureIntent, 0);

        views.setOnClickPendingIntent(R.id.widget_settings_button, widgetConfigurePendingIntent);
    }

    private static void setRefreshButtonOnClick(Context context, int appWidgetId, RemoteViews views) {
        Intent widgetRefreshIntent = new Intent(context, Widget.class).setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        widgetRefreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent widgetRefreshPendingIntent = PendingIntent.getBroadcast(context, 4, widgetRefreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget_refresh_button, widgetRefreshPendingIntent);
    }

    private static void updateWidget(final Context context, int appWidgetId) {
        ChannelSettingsManager channelSettingsManager = ChannelSettingsManager.getInstance(context);

        ChannelSettings channelSettings = channelSettingsManager.getChannelSettings(appWidgetId);
        if (channelSettings == null) {
            return;
        }
        Credentials credentials = channelSettings.getCredentials();
        int field = channelSettings.getFieldNr();

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        double minValueTrigger = channelSettings.getMinValue();
        double maxValueTrigger = channelSettings.getMaxValue();
        boolean minTrigger = channelSettings.isMinTrigger();
        boolean maxTrigger = channelSettings.isMaxTrigger();
        RequestManager.getInstance().requestFeed(credentials, context, 1, new VolleyCallback() {
            @Override
            public void onSuccess(ThingspeakResponse thingspeakResponse) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                String formattedDate = df.format(new Date());
                views.setTextViewText(R.id.widget_last_feed_time, formattedDate);
                views.setTextViewText(R.id.widget_field_title, thingspeakResponse.getChannel().getFields()[field - 1]);
                String value = thingspeakResponse.getFeeds()[0].getFields()[field - 1];
                views.setTextViewText(R.id.widget_value_button, String.format(Locale.US, "%.1f", Float.valueOf(value)));
                Notifier notifier = new Notifier();
                if (maxTrigger) {
                    if (Float.valueOf(value) >= maxValueTrigger) {
                        views.setImageViewResource(R.id.bell_top, R.drawable.bell_on);
                        notifier.sendNotification(context, appWidgetId, "Alarm: " + value + " is more than: " + maxValueTrigger);
                    } else {
                        views.setImageViewResource(R.id.bell_top, R.drawable.bell);
                    }
                } else if (minTrigger) {
                    if (Float.valueOf(value) <= minValueTrigger) {
                        views.setImageViewResource(R.id.bell_bottom, R.drawable.bell_on);
                        notifier.sendNotification(context, appWidgetId, "Alarm: " + value + " is less than: " + minValueTrigger);
                    } else {
                        views.setImageViewResource(R.id.bell_bottom, R.drawable.bell);
                    }
                } else {
                    views.setViewVisibility(R.id.bell_top, View.GONE);
                    views.setViewVisibility(R.id.bell_bottom, View.GONE);
                }
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onError(VolleyError error) {

            }

        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            //up(context, appWidgetId, appWidgetManager);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            ChannelSettingsManager.getInstance(context).removeChannelSettings(appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Bundle extras = intent.getExtras();

        if (extras != null) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                updateWidget(context, appWidgetId);
                setAlarm(context, appWidgetId);
            }

        }
    }


}

