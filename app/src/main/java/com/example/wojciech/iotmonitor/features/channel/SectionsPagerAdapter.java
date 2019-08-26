package com.example.wojciech.iotmonitor.features.channel;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.wojciech.iotmonitor.R;
import com.example.wojciech.iotmonitor.model.thingspeak.Credentials;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.hour, R.string.day, R.string.week, R.string.month};
    private final Context context;
    private final Credentials credentials;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Credentials credentials) {
        super(fm);
        this.context = context;
        this.credentials = credentials;
    }

    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position, credentials);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 4;
    }
}