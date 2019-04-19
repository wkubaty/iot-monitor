package com.example.wojciech.iotmonitor.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wojciech.iotmonitor.ChannelSettingsManager;
import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.viewmodel.MainViewModel;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private CredentialsRepository credentialsRepository;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChannelSettingsManager.getInstance(this);
        CredentialsRepository.getInstance(this);
        setContentView(R.layout.activity_main);
        credentialsRepository = CredentialsRepository.getInstance(this);
        initViewModel();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getCredentials().observe(this, new Observer<Set<Credentials>>() {
            @Override
            public void onChanged(@Nullable Set<Credentials> credentials) {
                Log.d("initViewModel", "onchange");
                if (credentials == null || credentials.isEmpty()) {
                    Toast.makeText(MainActivity.this, "no saved credentials", Toast.LENGTH_LONG).show();
                } else {
                    showChannels(credentials);
                }
            }
        });
    }

    @Override
    protected void onStart() {
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

    private void showChannels(Set<Credentials> credentials) {
        LinearLayout layout = findViewById(R.id.content_main);
        layout.removeAllViews();
        for (final Credentials creds : credentials) {
            Button channelButton = new Button(this);
            channelButton.setText(creds.getName());
            channelButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
                intent.putExtra("credentials", creds);
                startActivity(intent);
            });
            channelButton.setOnLongClickListener((View v) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(true);
                builder.setMessage("Are you sure you want to delete this channel?");
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
        credentialsRepository.removeCredentials(name);
//        showChannels(viewModel.getCredentials());

    }

}
