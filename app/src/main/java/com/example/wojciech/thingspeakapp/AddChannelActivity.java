package com.example.wojciech.thingspeakapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.wojciech.thingspeakapp.databinding.ActivityAddChannelBinding;
import com.example.wojciech.thingspeakapp.model.Credentials;
import com.example.wojciech.thingspeakapp.model.ThingspeakResponse;


public class AddChannelActivity extends AppCompatActivity {
    private int channeldId;
    private String apiKey;
    private ActivityAddChannelBinding bnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_add_channel);

    }

    public void searchChannel(View view) {

        channeldId = Integer.valueOf(bnd.channelId.getText().toString());
        apiKey = bnd.apiKey.getText().toString();

        try {

            Credentials credentials = new Credentials(channeldId, "", apiKey);
            RequestManager.getInstance().requestFeed(credentials, getApplicationContext(), 0, new VolleyCallback(){
                @Override
                public void onSuccess(ThingspeakResponse thingspeakResponse) {
                    Intent intent = new Intent();
                    credentials.setName(thingspeakResponse.getChannel().getName());
                    CredentialsManager.getInstance().addCredentials(AddChannelActivity.this, credentials);
                    intent.putExtra("credentials", credentials);
                    setResult(RESULT_OK, intent);
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
