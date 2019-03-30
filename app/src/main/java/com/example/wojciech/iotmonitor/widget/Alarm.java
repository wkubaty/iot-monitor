package com.example.wojciech.iotmonitor.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Alarm {

    private static final int REQUEST_CODE_ALARM = 10;

    public void setAlarm(Context context, int appWidgetId, int timeInMinutes) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Widget.class);
        intent.putExtra("widgetId", appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM,  intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ timeInMinutes*60*1000, pendingIntent);

    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Widget.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
