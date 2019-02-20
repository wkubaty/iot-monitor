package com.example.wojciech.thingspeakapp;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HourAxisValueFormatter implements IAxisValueFormatter {

    private DateFormat dateFormat;

    public HourAxisValueFormatter() {
        this.dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    }

    @Override
    public String getFormattedValue(float timestamp, AxisBase axis) {
        Date date = new Date();
        date.setTime((long) timestamp);
        return dateFormat.format(date);
    }
}