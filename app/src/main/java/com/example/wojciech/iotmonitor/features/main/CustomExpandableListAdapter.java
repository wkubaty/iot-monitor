package com.example.wojciech.iotmonitor.features.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.databinding.MainExpandableGroupBinding;
import com.example.wojciech.iotmonitor.databinding.MainExpandableItemBinding;
import com.example.wojciech.iotmonitor.features.channel.ChannelActivity;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<FieldValueListItem>> expandableListDetail;
    private List<Credentials> credentials;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<FieldValueListItem>> expandableListDetail, List<Credentials> credentials) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.credentials = credentials;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final FieldValueListItem expandedListTextItem = (FieldValueListItem) getChild(listPosition, expandedListPosition);
        LayoutInflater layoutInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MainExpandableItemBinding bnd = DataBindingUtil.inflate(layoutInflater, R.layout.main_expandable_item, null, false);

        bnd.tvExpandedListItemTitle.setText(expandedListTextItem.getField());
        bnd.tvExpandedListItemValue.setText(expandedListTextItem.getValue());
        return bnd.getRoot();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        LayoutInflater layoutInflater = (LayoutInflater) this.context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MainExpandableGroupBinding bnd = DataBindingUtil.inflate(layoutInflater, R.layout.main_expandable_group, null, false);
        bnd.tvListTitle.setTypeface(null, Typeface.BOLD);
        bnd.tvListTitle.setText(listTitle);
        bnd.ivChartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChannelActivity.class);
            intent.putExtra("credentials", credentials.get(listPosition));
            context.startActivity(intent);
        });
        return bnd.getRoot();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}