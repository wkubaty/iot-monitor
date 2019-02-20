package com.example.wojciech.thingspeakapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.wojciech.thingspeakapp.model.Channel;
import com.example.wojciech.thingspeakapp.model.Credentials;
import com.example.wojciech.thingspeakapp.model.Feed;
import com.example.wojciech.thingspeakapp.model.ThingspeakResponse;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ChannelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Intent intent = getIntent();
        Credentials credentials = (Credentials) intent.getSerializableExtra("credentials");
        RequestManager.getInstance().requestFeed(credentials, getApplicationContext(), 100, new VolleyCallback() {
            @Override
            public void onSuccess(ThingspeakResponse thingspeakResponse) {
                LinearLayout layout = findViewById(R.id.activity_channel);
                Channel channel = thingspeakResponse.getChannel();
                for(int i=0; i<8; i++) {
                    if(channel.getFields()[i] == null){
                        continue;
                    }
                    List<Entry> entries = new ArrayList<>();
                    View channelView = getLayoutInflater().inflate(R.layout.channel, null);
                    TextView channelTitle = channelView.findViewById(R.id.channel_title);
                    channelTitle.setText(channel.getFields()[i]);
                    LineChart lineChart = channelView.findViewById(R.id.channel_chart);
                    for (Feed feed : thingspeakResponse.getFeeds()) {
                        if(feed.getFields()[i] == null){
                            continue;
                        }
                        String myDate = feed.getCreatedAt();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        Date date = null;
                        try {
                            date = sdf.parse(myDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long timeInMillis = date.getTime();
                        entries.add(new Entry(timeInMillis, Float.valueOf(feed.getFields()[i])));

                    }


                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000);
                    LineDataSet dataSet = new LineDataSet(entries, channel.getFields()[i]);
                    dataSet.setCircleColor(Color.rgb(255, 0, 0));
                    dataSet.setColor(Color.rgb(255, 0, 0));
                    IAxisValueFormatter valueFormatter = new HourAxisValueFormatter();
                    lineChart.getXAxis().setValueFormatter(valueFormatter);
                    lineChart.setData(new LineData(dataSet));
                    lineChart.setLayoutParams(params);
                    lineChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    lineChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                    lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    layout.addView(channelView);
                    lineChart.invalidate();
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

}
