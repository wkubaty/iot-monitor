package com.example.wojciech.iotmonitor.features.main;

import com.example.wojciech.iotmonitor.utils.TimeHelper;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ChannelStatus {
    private String name;
    private Date lastUpdate;
    private UpdateTime updateTime;
    private TYPE type;

    public ChannelStatus(String name, Date lastUpdate) {
        this.name = name;
        this.lastUpdate = lastUpdate;
        setOrUpdate();
    }

    private static TYPE getType(long minutes) {
        if (minutes < 10) {
            return TYPE.RECENT;
        } else if (minutes < 60) {
            return TYPE.WARNING;
        }
        return TYPE.DANGER;
    }

    public void setOrUpdate() {
        long minutes = TimeHelper.getMinutesFromLastUpdate(lastUpdate);
        this.updateTime = new UpdateTime(minutes);
        this.type = getType(minutes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelStatus that = (ChannelStatus) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getLastUpdateString() {
        return updateTime.getUpdateTime();
    }

    public TYPE getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ChannelStatus{" +
                "name='" + name + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", updateTime=" + updateTime +
                ", type=" + type +
                '}';
    }

    private enum TIME_UNIT {
        m("m"), // minute
        h("h"), // hour
        d("d"); // day

        private final String unit;

        TIME_UNIT(String unit) {
            this.unit = unit;
        }

        @Override
        public String toString() {
            return unit;
        }
    }

    enum TYPE {
        RECENT,
        WARNING,
        DANGER

    }

    private class UpdateTime {
        private long time;
        private TIME_UNIT timeUnit;

        public UpdateTime(long minutes) {
            if (minutes < 60) {
                this.time = minutes;
                this.timeUnit = TIME_UNIT.m;
            } else if (minutes < 24 * 60) {
                this.time = minutes / 60;
                this.timeUnit = TIME_UNIT.h;
            } else {
                this.time = minutes / 60 / 24;
                this.timeUnit = TIME_UNIT.d;
            }
        }


        public String getUpdateTime() {
            //TODO time < 0
            if (time == 0 && timeUnit.equals(TIME_UNIT.m)) {
                return "<1 m";
            } else if (time > 365 && timeUnit.equals(TIME_UNIT.d)) {
                return ">365 d";
            } else {
                return String.format(Locale.ENGLISH, "%d %s", time, timeUnit);
            }
        }

        @Override
        public String toString() {
            return "UpdateTime{" +
                    "time=" + time +
                    ", timeUnit=" + timeUnit +
                    '}';
        }
    }
}
