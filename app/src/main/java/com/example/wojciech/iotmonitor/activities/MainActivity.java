package com.example.wojciech.iotmonitor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wojciech.iotmonitor.ChannelSettingsManager;
import com.example.wojciech.iotmonitor.CredentialsManager;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChannelSettingsManager.initialize(this);
        CredentialsManager.initialize(this);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        HashSet<Credentials> credentials = CredentialsManager.getInstance().getCredentials();

        if (credentials.isEmpty()){
            Toast.makeText(this, "no saved credentials", Toast.LENGTH_LONG).show();
        } else {
            showChannels(credentials);
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

        if (item.getItemId() == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addChannel(View view) {
        Intent intent = new Intent(this, AddChannelActivity.class);
        startActivity(intent);
    }

    private void showChannels(HashSet<Credentials> credentials){
        LinearLayout layout = findViewById(R.id.content_main);
        layout.removeAllViews();
        for(final Credentials creds : credentials){
            Button channelButton = new Button(this);
            channelButton.setText(creds.getName());
            channelButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
                intent.putExtra("credentials", creds);
                startActivity(intent);
            });
            channelButton.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(true);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                    deleteChannel(channelButton.getText().toString());
                    dialog.cancel();
                });
                builder.setNegativeButton(R.string.label_cancel, (dialog, which) -> {
                    dialog.cancel();

                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;

            });
            layout.addView(channelButton);
        }
    }

    private void deleteChannel(String name) {
        Credentials credentials = CredentialsManager.getInstance().getCredentials().stream()
                .filter(c-> c.getName().equals(name))
                .findFirst().orElse(null);
        if(credentials!=null){
            CredentialsManager.getInstance().removeCredentials(credentials.getId());
            showChannels(CredentialsManager.getInstance().getCredentials());
        }
    }

}
