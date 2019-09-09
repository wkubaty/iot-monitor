package com.example.wojciech.iotmonitor.utils;

import android.databinding.InverseMethod;

public class Converter {

    @InverseMethod("floatToString")
    public static Float stringToFloat(String s) {
        return Float.valueOf(s);
    }

    public static String floatToString(float f) {
        return String.valueOf(f);
    }
}
