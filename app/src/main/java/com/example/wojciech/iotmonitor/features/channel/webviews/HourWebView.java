package com.example.wojciech.iotmonitor.features.channel.webviews;

import android.net.Uri;

import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HourWebView extends AbstractWebView {
    private static final String TAG = HourWebView.class.getSimpleName();

    public HourWebView(Credentials credentials) {
        super(credentials);
    }


    @Override
    public String getUrl(int fieldNr, int width, int height, float density) {
        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getRawOffset();

        String timeZone = String.format(Locale.ENGLISH, "%s%02d.%02d", offset >= 0 ? "+" : "-", offset / 3600000, (offset / 60000) % 60);
        Uri.Builder builder = getPrebuiltUrl(fieldNr, width, height, density);
        builder.appendQueryParameter("start", getStartDate())
                .appendQueryParameter("offset", timeZone);

        return builder.build().toString();
    }

    private String getStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return sdf.format(calendar.getTime());
    }
}
