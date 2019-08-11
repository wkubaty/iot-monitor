package com.example.wojciech.iotmonitor.features.adding.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.FragmentTabAddMultipleChannelsBinding;
import com.example.wojciech.iotmonitor.features.adding.IChannelProvider;
import com.example.wojciech.iotmonitor.features.adding.viewmodel.TabMultipleChannelsFragmentViewModel;

public class TabMultipleChannelsFragment extends Fragment {
    private TabMultipleChannelsFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentTabAddMultipleChannelsBinding bnd = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_add_multiple_channels, container, false);
        viewModel = ViewModelProviders.of(this).get(TabMultipleChannelsFragmentViewModel.class);
        viewModel.init((IChannelProvider) getActivity());
        bnd.searchButtonAuto.setOnClickListener(v -> {
            viewModel.getChannelsWithUserApiKey(bnd.apiKeyAuto.getText().toString());
        });
        return bnd.getRoot();
    }
}
