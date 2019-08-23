package com.example.wojciech.iotmonitor.features.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.wojciech.iotmonitor.ChannelsAdapter;
import com.example.wojciech.iotmonitor.CredentialsRepository;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.ActivityMainBinding;
import com.example.wojciech.iotmonitor.features.adding.ui.AddChannelActivity;
import com.example.wojciech.iotmonitor.features.channel.ChannelActivity;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements ChannelsAdapter.AdapterOnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CredentialsRepository credentialsRepository;
    private MainViewModel viewModel;
    private List<Credentials> credentials = new ArrayList<>();
    private ActivityMainBinding bnd;
    private ArrayList<String> expandableListTitle;
    private CustomExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bnd.setLifecycleOwner(this);
        credentialsRepository = CredentialsRepository.getInstance(this);

        initViewModel();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.init();
        bnd.setLifecycleOwner(this);
        bnd.setViewmodel(viewModel);
        viewModel.getCredentials().observe(this, new Observer<Set<Credentials>>() {
            @Override
            public void onChanged(@Nullable Set<Credentials> creds) {
                credentials.clear();
                credentials.addAll(creds);
                credentials.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
                viewModel.fetchChannelsData(credentials);

                viewModel.getExpandableListDetailLiveData().observe(MainActivity.this, new Observer<HashMap<String, List<FieldValueListItem>>>() {
                    @Override
                    public void onChanged(@Nullable HashMap<String, List<FieldValueListItem>> stringListHashMap) {
                        if (stringListHashMap != null) {
                            expandableListTitle = new ArrayList<>(stringListHashMap.keySet());
                            expandableListTitle.sort(String::compareTo);
                        }

                        expandableListAdapter = new CustomExpandableListAdapter(MainActivity.this, expandableListTitle, stringListHashMap, credentials);
                        bnd.expandableListView.setAdapter(expandableListAdapter);
                        bnd.expandableListView.setOnGroupExpandListener(groupPosition -> {
                        });

                    }

                });

                viewModel.credentialsListChanged();
            }
        });
        bnd.mainRefreshLayout.setOnRefreshListener(() -> {
            viewModel.fetchChannelsData(credentials);
            bnd.mainRefreshLayout.setRefreshing(false);
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
