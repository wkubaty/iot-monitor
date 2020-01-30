package com.example.wojciech.iotmonitor.features.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.wojciech.iotmonitor.ChannelsAdapter;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.ActivityMainBinding;
import com.example.wojciech.iotmonitor.features.adding.ui.AddChannelActivity;
import com.example.wojciech.iotmonitor.features.channel.ChannelActivity;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.model.thingspeak.FieldSettings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChannelsAdapter.AdapterOnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel viewModel;
    private List<Credentials> credentials = new ArrayList<>();
    private ActivityMainBinding bnd;
    private ArrayList<ChannelStatus> expandableChannelList;
    private CustomExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bnd.setLifecycleOwner(this);
        initViewModel();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.init();
        bnd.setLifecycleOwner(this);
        bnd.setViewmodel(viewModel);

        viewModel.getCredentialsLive().observe(this, creds -> {
            credentials = creds;
            viewModel.credentialsListChanged();
            viewModel.fetchChannelsData(creds);
        });

        viewModel.getFieldSettingsLive().observe(this, new Observer<List<FieldSettings>>() {
            @Override
            public void onChanged(@Nullable List<FieldSettings> fieldSettings) {
            }
        });

        viewModel.getExpandableListDetailLiveData().observe(this, stringListHashMap -> {
            expandableChannelList = new ArrayList<>(stringListHashMap.keySet());
            expandableChannelList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            expandableListAdapter = new CustomExpandableListAdapter(MainActivity.this, expandableChannelList, stringListHashMap, credentials);
            bnd.expandableListView.setAdapter(expandableListAdapter);
            bnd.expandableListView.setOnGroupExpandListener(groupPosition -> {

            });
            bnd.expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

                int channelId = credentials.get(groupPosition).getId();
                FieldSettings settings = viewModel.getChannelFieldSettingsByChannelIdAndField(channelId, childPosition + 1);
                FieldSettingsDialogFragment fieldSettingsDialogFragment = FieldSettingsDialogFragment.newInstance(settings);
                fieldSettingsDialogFragment.setOnDialogButtonClick(new FieldSettingsDialogFragment.OnDialogButtonClick() {
                    @Override
                    public void onPositiveClicked(FieldSettings fieldSettingsTmp) {
                        viewModel.updateFieldSetting(fieldSettingsTmp);
                    }
                    @Override
                    public void onNegativeClicked() {

                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                    }


                });
                fieldSettingsDialogFragment.show(getSupportFragmentManager(), "triggers_dialog");
                return false;
            });
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
        viewModel.deleteByChannelId(id);
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
                .setCancelable(true)
                .setMessage(getString(R.string.confirm_field_deleting))
                .setPositiveButton(R.string.label_yes, (dialog, which) -> {
                    deleteChannel(credentials.get(position).getId());
                    dialog.cancel();
                }).setNegativeButton(R.string.label_cancel, (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clear();
    }
}
