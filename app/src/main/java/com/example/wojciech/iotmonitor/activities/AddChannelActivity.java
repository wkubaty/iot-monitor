package com.example.wojciech.iotmonitor.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.ActivityAddChannelBinding;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.model.thingspeak.ThingspeakResponse;
import com.example.wojciech.iotmonitor.net.RequestManager;
import com.example.wojciech.iotmonitor.net.VolleyCallback;
import com.example.wojciech.iotmonitor.viewmodel.AddChannelViewModel;


public class AddChannelActivity extends AppCompatActivity {
    private int channeldId;
    private String apiKey;
    private ActivityAddChannelBinding bnd;
    private AddChannelViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_add_channel);
        initViewModel();

    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AddChannelViewModel.class);
    }

    public void searchChannel(View view) {
        channeldId = Integer.valueOf(bnd.channelId.getText().toString());
        apiKey = bnd.apiKey.getText().toString();

        try {
            Credentials credentials = new Credentials(channeldId, apiKey);
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
//                    setResult(RESULT_CANCELED, new Intent().putExtra("error", error));
//                    finish();
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
