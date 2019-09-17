package com.example.wojciech.iotmonitor;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wojciech.iotmonitor.databinding.ChannelListItemBinding;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.util.List;

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {

    private List<Credentials> credentials;
    private AdapterOnClickListener adapterOnClickListener;

    public ChannelsAdapter(List<Credentials> credentials, AdapterOnClickListener adapterOnClickListener) {
        this.credentials = credentials;
        this.adapterOnClickListener = adapterOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ChannelListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.channel_list_item, parent, false);
        return new ViewHolder(binding, adapterOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(credentials.get(i));
    }

    @Override
    public int getItemCount() {
        return credentials == null ? 0 : credentials.size();
    }

    public interface AdapterOnClickListener {
        void onChannelClick(int position);

        boolean onChannelLongClick(int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final ChannelListItemBinding binding;
        private AdapterOnClickListener adapterOnClickListener;

        public ViewHolder(@NonNull ChannelListItemBinding itemBinding, AdapterOnClickListener adapterOnClickListener) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            this.adapterOnClickListener = adapterOnClickListener;

        }

        public void bind(Credentials item) {
            binding.btnChannelName.setOnClickListener(this);
            binding.btnChannelName.setOnLongClickListener(this);
            binding.setItem(item);
        }

        @Override
        public void onClick(View v) {
            adapterOnClickListener.onChannelClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return adapterOnClickListener.onChannelLongClick(getAdapterPosition());
        }

    }

}
