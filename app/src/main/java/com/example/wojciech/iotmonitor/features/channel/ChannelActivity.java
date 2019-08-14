package com.example.wojciech.iotmonitor.features.channel;

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
import com.example.wojciech.iotmonitor.features.channel.webviews.AbstractWebView;
import com.example.wojciech.iotmonitor.features.channel.webviews.DayWebView;
import com.example.wojciech.iotmonitor.features.channel.webviews.HourWebView;
import com.example.wojciech.iotmonitor.features.channel.webviews.MonthWebView;
import com.example.wojciech.iotmonitor.features.channel.webviews.WeekWebView;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.util.List;

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
        initRecyclerView();
        initViewModel();
        showWebView(new HourWebView(credentials));

        bnd.buttonChartTimeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            showWebView(new HourWebView(credentials));
        });
        bnd.buttonChartTimeRangeHour.setOnClickListener(v -> setWebView(new HourWebView(credentials)));
        bnd.buttonChartTimeRangeDay.setOnClickListener(v -> setWebView(new DayWebView(credentials)));
        bnd.buttonChartTimeRangeWeek.setOnClickListener(v -> setWebView(new WeekWebView(credentials)));
        bnd.buttonChartTimeRangeMonth.setOnClickListener(v -> setWebView(new MonthWebView(credentials)));


        bnd.buttonChartTimeRangeHour.setChecked(true);
        bnd.channelToolbar.setTitle(credentials.getName());
        bnd.channelToolbar.setNavigationOnClickListener(v -> onBackPressed());
        initRecyclerView();
        initViewModel();
        setWebView(new HourWebView(credentials));

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

    private void setWebView(AbstractWebView webView) {
        ViewTreeObserver vto = bnd.linearLayoutChannel.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout layout = bnd.linearLayoutChannel;
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                showWebView(webView);
            }
        });
    }

    private void showWebView(AbstractWebView webView) {
        int width = bnd.linearLayoutChannel.getMeasuredWidth();
        int height = bnd.linearLayoutChannel.getMeasuredHeight();
        if (height == 0 || width == 0) {
            return;
        }
        float density = getResources().getDisplayMetrics().density;
        viewModel.showWebView(webView, width, height, density);
    }

}
