package com.example.wojciech.iotmonitor.widget;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.wojciech.iotmonitor.ChannelSettingsManager;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.features.channel.ChannelActivity;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import static android.support.v4.content.ContextCompat.getSystemService;

public class Notifier {

    private static final String NOTIFICATION_CHANNEL_ID = "TRIGGER";

    public void sendNotification(Context context, int appWidgetId, String contentText) {
        createNotificationChannel(context);
        Intent intent = new Intent(context, ChannelActivity.class);
        Credentials credentials = ChannelSettingsManager.getInstance(context).getChannelSettings(appWidgetId).getCredentials();

        intent.putExtra("credentials", credentials);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 20, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_warning_24dp)
                .setContentTitle("Warning")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(12345, mBuilder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


}
