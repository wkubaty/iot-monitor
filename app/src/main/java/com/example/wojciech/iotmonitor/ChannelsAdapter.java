package com.example.wojciech.iotmonitor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        View view = inflater.inflate(R.layout.channel_list_item, parent, false);
        return new ViewHolder(view, adapterOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.button.setText(credentials.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return credentials.size();
    }

    public interface AdapterOnClickListener {
        void onChannelClick(int position);

        boolean onChannelLongClick(int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        Button button;
        AdapterOnClickListener adapterOnClickListener;

        public ViewHolder(@NonNull View itemView, AdapterOnClickListener adapterOnClickListener) {
            super(itemView);
            this.button = itemView.findViewById(R.id.channel_name_button);
            this.adapterOnClickListener = adapterOnClickListener;
            button.setOnClickListener(this);
            button.setOnLongClickListener(this);
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
