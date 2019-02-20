package com.example.wojciech.thingspeakapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wojciech.thingspeakapp.model.Credentials;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CHANNEL_CODE = 0;
    static final String channels_prefs = "CHANNEL_PREFS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        ArrayList<Credentials> credentials = CredentialsManager.getInstance().getCredentials(this);

        if (!credentials.isEmpty()){
            showChannels(credentials);
        }
        else{
            Toast.makeText(this, "no saved credentials", Toast.LENGTH_LONG).show();
        }
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addChannel(View view) {
        Intent intent = new Intent(this, AddChannelActivity.class);
        startActivityForResult(intent, ADD_CHANNEL_CODE);
    }

    private void showChannels(List<Credentials> credentials){
        LinearLayout layout = findViewById(R.id.content_main);
        layout.removeAllViews();
        for(final Credentials creds : credentials){
            Button channelButton = new Button(this);
            channelButton.setText(creds.getName());
            channelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
                    intent.putExtra("credentials", creds);
                    startActivity(intent);
                }
            });
            layout.addView(channelButton);
        }
    }


}
