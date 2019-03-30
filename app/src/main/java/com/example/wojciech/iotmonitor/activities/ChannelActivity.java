package com.example.wojciech.iotmonitor.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.ActivityChannelBinding;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChannelActivity extends AppCompatActivity {
    private ActivityChannelBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Intent intent = getIntent();
        Credentials credentials = (Credentials) intent.getSerializableExtra("credentials");

        bnd = DataBindingUtil.setContentView(this, R.layout.activity_channel);
        bnd.buttonChartTimeGroup.setOnCheckedChangeListener((group, checkedId) ->
                setWebView(credentials, getStartDate()));
        bnd.buttonChartTimeRangeHour.setChecked(true);

    }
    private void setWebView(Credentials credentials, String start){
        ViewTreeObserver vto = bnd.linearLayoutChannel.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout layout = bnd.linearLayoutChannel;

                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                showWebView(credentials, start);

            }
        });
        showWebView(credentials, start);
    }

    private void showWebView(Credentials credentials, String start) {
        LinearLayout layout = bnd.linearLayoutChannelInflated;
        layout.removeAllViews();
        for(int i=1;i<=8;i++) {
            View channelView = getLayoutInflater().inflate(R.layout.channel, null);
            WebView channel = channelView.findViewById(R.id.webview);
            int width = bnd.linearLayoutChannel.getMeasuredWidth();
            int height = bnd.linearLayoutChannel.getMeasuredHeight();
            if(height==0 || width==0){
                return;
            }
            Uri.Builder builder = new Uri.Builder();
            float density = getResources().getDisplayMetrics().density;
            builder.scheme("https")
                    .authority("api.thingspeak.com")
                    .appendPath("channels")
                    .appendPath(String.valueOf(credentials.getId()))
                    .appendPath("charts")
                    .appendPath(String.valueOf(i))
                    .appendQueryParameter("api_key", credentials.getApiKey())
                    .appendQueryParameter("start", start)
                    .appendQueryParameter("offset", "+02.0")
                    .appendQueryParameter("height", String.valueOf((int) (height / density) - 30))
                    .appendQueryParameter("width", String.valueOf((int) (width / density)))
                    .appendQueryParameter("type", "spline")
                    .appendQueryParameter("color", "FF3300")
                    .appendQueryParameter("bgcolor", "FFFFFF");

            channel.getSettings().setJavaScriptEnabled(true);
            channel.loadUrl(builder.build().toString());

            layout.addView(channelView);
        }
    }

    private String getStartDate() {
        int amount = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if(bnd.buttonChartTimeRangeMonth.isChecked()){
            calendar.add(Calendar.MONTH, - amount);
        } else if(bnd.buttonChartTimeRangeWeek.isChecked()){
            calendar.add(Calendar.WEEK_OF_YEAR, - amount);
        } else if(bnd.buttonChartTimeRangeDay.isChecked()){
            calendar.add(Calendar.DAY_OF_MONTH, - amount);
        }
        else {
            calendar.add(Calendar.HOUR_OF_DAY, - amount);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        //sdf.setTimeZone(calendar.getTimeZone());
        return sdf.format(calendar.getTime());
    }
}
