package com.example.wojciech.iotmonitor.features.channel;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.ActivityChannelBinding;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

public class ChannelActivity extends AppCompatActivity {
    private static final String TAG = ChannelActivity.class.getSimpleName();
    private ActivityChannelBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Intent intent = getIntent();
        Credentials credentials = intent.getParcelableExtra("credentials");

        bnd = DataBindingUtil.setContentView(this, R.layout.activity_channel);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), credentials);
        bnd.viewPager.setAdapter(sectionsPagerAdapter);
        bnd.tabs.setupWithViewPager(bnd.viewPager);

        bnd.channelToolbar.setTitle(credentials.getName());
        bnd.channelToolbar.setNavigationOnClickListener(v -> onBackPressed());


    }


}
