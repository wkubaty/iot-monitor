package com.example.wojciech.iotmonitor.features.adding.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.ActivityAddChannelBinding;
import com.example.wojciech.iotmonitor.features.adding.IChannelProvider;
import com.example.wojciech.iotmonitor.features.adding.adapter.SectionsPagerAdapter;
import com.example.wojciech.iotmonitor.features.adding.viewmodel.AddChannelViewModel;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.model.thingspeak.ThingspeakResponse;
import com.example.wojciech.iotmonitor.net.RequestManager;
import com.example.wojciech.iotmonitor.net.VolleyCallback;


public class AddChannelActivity extends AppCompatActivity implements IChannelProvider {
    private ActivityAddChannelBinding bnd;
    private AddChannelViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_add_channel);
        initViewModel();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new TabMultipleChannelsFragment());
        sectionsPagerAdapter.addFragment(new TabSingleChannelFragment());
        bnd.viewPager.setAdapter(sectionsPagerAdapter);
        bnd.tabs.setupWithViewPager(bnd.viewPager);

    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AddChannelViewModel.class);
    }

    @Override
    public void addChannel(int channelId, String apiKey) {
        try {
            Credentials credentials = new Credentials(channelId, apiKey);
            RequestManager.getInstance().requestFeed(credentials, getApplicationContext(), 0, new VolleyCallback() {
                @Override
                public void onSuccess(ThingspeakResponse thingspeakResponse) {
                    credentials.setName(thingspeakResponse.getChannel().getName());
                    viewModel.addCredentials(credentials);
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onError(VolleyError error) {
                    Toast.makeText(AddChannelActivity.this, "Wrong credentials, try again", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failure: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

}