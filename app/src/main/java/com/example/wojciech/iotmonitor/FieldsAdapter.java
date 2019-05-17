package com.example.wojciech.iotmonitor;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.wojciech.iotmonitor.databinding.FieldBinding;

import java.util.List;

public class FieldsAdapter extends RecyclerView.Adapter<FieldsAdapter.ViewHolder> {
    public static final String TAG = FieldsAdapter.class.getSimpleName();
    private List<String> urls;

    public FieldsAdapter(List<String> urls) {
        this.urls = urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        FieldBinding fieldBinding = DataBindingUtil.inflate(layoutInflater, R.layout.field, viewGroup, false);

        return new ViewHolder(fieldBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(urls.get(i));
    }

    @Override
    public int getItemCount() {
        return urls == null ? 0 : urls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final FieldBinding binding;

        public ViewHolder(@NonNull FieldBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

        public void bind(String url) {
            binding.webview.getSettings().setJavaScriptEnabled(true);
            binding.webview.loadUrl(url);

        }
    }
}
