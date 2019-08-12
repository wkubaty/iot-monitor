package com.example.wojciech.iotmonitor.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.wojciech.iotmonitor.FieldsAdapter;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.ActivityChannelBinding;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;
import com.example.wojciech.iotmonitor.viewmodel.ChannelViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChannelActivity extends AppCompatActivity {
    private static final String TAG = ChannelActivity.class.getSimpleName();
    private ActivityChannelBinding bnd;
    private ChannelViewModel viewModel;
    private FieldsAdapter fieldsAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Intent intent = getIntent();
        Credentials credentials = (Credentials) intent.getSerializableExtra("credentials");

        bnd = DataBindingUtil.setContentView(this, R.layout.activity_channel);
        bnd.buttonChartTimeGroup.setOnCheckedChangeListener((group, checkedId) ->
                showWebView(credentials, getStartDate()));
        bnd.buttonChartTimeRangeHour.setChecked(true);
        bnd.channelToolbar.setTitle(credentials.getName());
        initRecyclerView();
        initViewModel();
        setWebView(credentials, getStartDate());

    }

    private void initRecyclerView() {
        recyclerView = bnd.recyclerViewFields;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
        viewModel.getUrls().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> news) {

                if (fieldsAdapter == null) {
                    fieldsAdapter = new FieldsAdapter(news);
                    recyclerView.setAdapter(fieldsAdapter);
                } else {
                    fieldsAdapter.setUrls(news);
                    fieldsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setWebView(Credentials credentials, String start) {
        ViewTreeObserver vto = bnd.linearLayoutChannel.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout layout = bnd.linearLayoutChannel;
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                showWebView(credentials, start);
            }
        });
    }

    private void showWebView(Credentials credentials, String start) {
        int width = bnd.linearLayoutChannel.getMeasuredWidth();
        int height = bnd.linearLayoutChannel.getMeasuredHeight();
        if (height == 0 || width == 0) {
            return;
        }
        float density = getResources().getDisplayMetrics().density;
        viewModel.showWebView(credentials, start, width, height, density);
    }

    private String getStartDate() {
        int amount = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (bnd.buttonChartTimeRangeMonth.isChecked()) {
            calendar.add(Calendar.MONTH, -amount);
        } else if (bnd.buttonChartTimeRangeWeek.isChecked()) {
            calendar.add(Calendar.WEEK_OF_YEAR, -amount);
        } else if (bnd.buttonChartTimeRangeDay.isChecked()) {
            calendar.add(Calendar.DAY_OF_MONTH, -amount);
        } else {
            calendar.add(Calendar.HOUR_OF_DAY, -amount);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        //sdf.setTimeZone(calendar.getTimeZone());
        return sdf.format(calendar.getTime());
    }
}
