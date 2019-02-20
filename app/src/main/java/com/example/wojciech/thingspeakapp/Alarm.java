package com.example.wojciech.thingspeakapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.android.volley.VolleyError;
import com.example.wojciech.thingspeakapp.model.ChannelSettings;
import com.example.wojciech.thingspeakapp.model.Credentials;
import com.example.wojciech.thingspeakapp.model.ThingspeakResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.support.v4.content.ContextCompat.getSystemService;

public class Alarm extends BroadcastReceiver {
    private static final String NOTIFICATION_CHANNEL_ID = "TRIGGER";
    private float MIN_VALUE_TRIGGER = 0.0f;
    private float MAX_VALUE_TRIGGER = 0.0f;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int appWidgetId = intent.getIntExtra("widgetId",0);
        updateWidgetAndSetNewAlarm(context, appWidgetId);
    }

    public void updateWidgetAndSetNewAlarm(Context context, int appWidgetId){
        ChannelSettings channelSettings = ThingspeakWidgetConfigure.loadChannelSettings(context, appWidgetId);
        if(channelSettings != null){
            MIN_VALUE_TRIGGER = channelSettings.getMinValue();
            MAX_VALUE_TRIGGER = channelSettings.getMaxValue();
            Credentials credentials = channelSettings.getCredentials();
            int field = channelSettings.getFieldNr();
            if(credentials != null){
                setAlarm(context, appWidgetId);
                updateWidget(context, credentials, field, appWidgetId);
            }
        }
    }
    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
            CharSequence name = "channel_name";
//            String description = getString(R.string.channel_description);
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(Context context, String contentText) {
        createNotificationChannel(context);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 20, intent, 0);
        Intent snoozeIntent = new Intent(context, MainActivity.class);
//        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 11, snoozeIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_warning_24dp)
                .setContentTitle("Warning")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
//                .addAction(R.drawable.ic_snooze, getString(R.string.snooze), snoozePendingIntent);
                .addAction(R.drawable.ic_watch_later_black_24dp, "snooze", snoozePendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(12345, mBuilder.build());
    }

    private void updateWidget(final Context context, Credentials credentials, int field, int appWidgetId) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RequestManager.getInstance().requestFeed(credentials, context, 1, new VolleyCallback() {
            @Override
            public void onSuccess(ThingspeakResponse thingspeakResponse) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.thingspeak_widget);
                Date d1 = new Date();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                String formattedDate = df.format(d1);
                views.setTextViewText(R.id.widget_last_feed_time, formattedDate);
                views.setTextViewText(R.id.widget_field_title, thingspeakResponse.getChannel().getFields()[field-1]);
                String value = thingspeakResponse.getFeeds()[0].getFields()[field-1];
                views.setTextViewText(R.id.appwidget_button, String.format(Locale.US, "%.1f", Float.valueOf(value)));
                if(Float.valueOf(value) <= MIN_VALUE_TRIGGER){
                    sendNotification(context, "alarm: " + value + " is less than: " + MIN_VALUE_TRIGGER);
                } else if(Float.valueOf(value) >= MAX_VALUE_TRIGGER){
                    sendNotification(context, "alarm: " + value + " is more than: " + MIN_VALUE_TRIGGER);
                }
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onError(VolleyError error) {

            }

        });
    }

    public void setAlarm(Context context, int appWidgetId) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra("widgetId", appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId,  intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ChannelSettings channelSettings = ThingspeakWidgetConfigure.loadChannelSettings(context, appWidgetId);
        if(channelSettings != null){
            int refreshTime = channelSettings.getRefreshTime();
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ refreshTime*60*1000, pendingIntent);
        }

    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
