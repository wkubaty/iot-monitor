package com.example.wojciech.iotmonitor.features.channel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.wojciech.iotmonitor.FieldsAdapter;
import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.FragmentChannelBinding;
import com.example.wojciech.iotmonitor.features.channel.webviews.AbstractWebView;
import com.example.wojciech.iotmonitor.features.channel.webviews.WebViewFactory;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CREDENTIALS = "credentials";
    private static final String TAG = PlaceholderFragment.class.getSimpleName();

    private ChannelPagerViewModel channelPagerViewModel;

    private FragmentChannelBinding bnd;
    private FieldsAdapter fieldsAdapter;
    private AbstractWebView webView;
    private int index;
    private Credentials credentials;

    public static PlaceholderFragment newInstance(int index, Credentials credentials) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putParcelable(ARG_CREDENTIALS, credentials);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        index = 1;
        credentials = null;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            credentials = getArguments().getParcelable(ARG_CREDENTIALS);
        }
        if (credentials == null) {
            Log.d(TAG, "Could not display webview. No such credentials.");
        }

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        channelPagerViewModel = ViewModelProviders.of(this).get(ChannelPagerViewModel.class);
        webView = WebViewFactory.getWebView(index, credentials);
        Log.d(TAG, "onCreateView: ");
        bnd = DataBindingUtil.inflate(inflater, R.layout.fragment_channel, container, false);
        initRecyclerView();
        initViewModel();
        setWebView(webView);
        return bnd.getRoot();
    }


    private void initRecyclerView() {
        bnd.recyclerViewFields.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        bnd.recyclerViewFields.setLayoutManager(layoutManager);

    }

    private void initViewModel() {
        fieldsAdapter = new FieldsAdapter(new ArrayList<>()); //todo
        bnd.recyclerViewFields.setAdapter(fieldsAdapter);
        channelPagerViewModel.getUrls().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> news) {

                if (fieldsAdapter == null) {
                    fieldsAdapter = new FieldsAdapter(news);
                    bnd.recyclerViewFields.setAdapter(fieldsAdapter);
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
                bnd.linearLayoutChannel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
        channelPagerViewModel.showWebView(webView, width, height, density);
    }

}