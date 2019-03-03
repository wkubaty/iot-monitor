package com.example.wojciech.thingspeakapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class ThingspeakWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, intent, 0);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.thingspeak_widget);
        views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent);

        Intent widgetConfigureIntent = new Intent(context, ThingspeakWidgetConfigure.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        widgetConfigureIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent widgetConfigurePendingIntent = PendingIntent.getActivity(context, 3, widgetConfigureIntent, 0);

        views.setOnClickPendingIntent(R.id.widget_settings_button, widgetConfigurePendingIntent);

        Intent widgetRefreshIntent = new Intent(context, ThingspeakWidget.class).setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        widgetRefreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent widgetRefreshPendingIntent = PendingIntent.getBroadcast(context, 4, widgetRefreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget_refresh_button, widgetRefreshPendingIntent);

        up(context, appWidgetId, appWidgetManager, views);

    }

    private static void up(Context context, int appWidgetId, AppWidgetManager appWidgetManager, RemoteViews views) {
        Alarm alarm = new Alarm();
        alarm.cancelAlarm(context);
        alarm.updateWidgetAndSetNewAlarm(context, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, views);
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
            ThingspeakWidgetConfigure.deleteChannelSettings(context, appWidgetId);
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
            int appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.thingspeak_widget);
            up(context, appWidgetId, appWidgetManager, views);
        }
    }

}

