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
import com.example.wojciech.iotmonitor.databinding.FragmentTabAddSingleChannelBinding;
import com.example.wojciech.iotmonitor.features.adding.IChannelProvider;
import com.example.wojciech.iotmonitor.features.adding.viewmodel.TabSingleChannelFragmentViewModel;

public class TabSingleChannelFragment extends Fragment {
    private TabSingleChannelFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentTabAddSingleChannelBinding bnd = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_add_single_channel, container, false);
        viewModel = ViewModelProviders.of(this).get(TabSingleChannelFragmentViewModel.class);
        viewModel.init((IChannelProvider) getActivity());
        bnd.setViewmodel(viewModel);
        bnd.setLifecycleOwner(this);
        bnd.btnSearchManual.setOnClickListener(v -> {
            viewModel.getChannelsWithUserApiKey(Integer.parseInt(bnd.etChannelIdManual.getText().toString()), bnd.etApiKeyManual.getText().toString());
        });
        bnd.cbPrivateManual.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setPrivateChannel(isChecked));
        return bnd.getRoot();
    }
}
