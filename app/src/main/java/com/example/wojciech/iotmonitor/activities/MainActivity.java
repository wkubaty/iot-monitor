package com.example.wojciech.iotmonitor.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.wojciech.iotmonitor.ChannelsAdapter;
import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.ActivityMainBinding;
import com.example.wojciech.iotmonitor.features.adding.ui.AddChannelActivity;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements ChannelsAdapter.AdapterOnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CredentialsRepository credentialsRepository;
    private MainViewModel viewModel;
    private ChannelsAdapter channelsAdapter;
    private RecyclerView recyclerView;
    private List<Credentials> credentials = new ArrayList<>();
    private ActivityMainBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bnd.setLifecycleOwner(this);
        credentialsRepository = CredentialsRepository.getInstance(this);
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        bnd.setViewmodel(viewModel);
        viewModel.getCredentials().observe(this, new Observer<Set<Credentials>>() {
            @Override
            public void onChanged(@Nullable Set<Credentials> creds) {
                Log.d(TAG, "onChanged: initviewmodel");
                credentials.clear();
                credentials.addAll(creds);
                credentials.sort(new Comparator<Credentials>() {
                    @Override
                    public int compare(Credentials o1, Credentials o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                if (channelsAdapter == null) {
                    channelsAdapter = new ChannelsAdapter(credentials, MainActivity.this);
                    recyclerView.setAdapter(channelsAdapter);
                } else {
                    channelsAdapter.notifyDataSetChanged();
                }
                if (credentials == null || credentials.isEmpty()) {
                    Toast.makeText(MainActivity.this, "no saved credentials", Toast.LENGTH_LONG).show();
                }
                viewModel.credentialsListChanged();
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


    private void deleteChannel(int id) {
        credentialsRepository.removeCredentials(id);

    }

    @Override
    public void onChannelClick(int position) {
        Intent intent = new Intent(this, ChannelActivity.class);
        intent.putExtra("credentials", credentials.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onChannelLongClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(true);
        builder.setMessage("Are you sure you want to delete this field?");
        builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
            deleteChannel(credentials.get(position).getId());
            dialog.cancel();
        });
        builder.setNegativeButton(R.string.label_cancel, (dialog, which) -> {
            dialog.cancel();

        });
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }
}
