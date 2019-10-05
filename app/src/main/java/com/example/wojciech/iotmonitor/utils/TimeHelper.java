package com.example.wojciech.iotmonitor.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeHelper {

    public static long getMinutesFromLastUpdate(Date lastUpdate){
        long diffInMillis =  new Date().getTime() - lastUpdate.getTime();
        return TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
}
