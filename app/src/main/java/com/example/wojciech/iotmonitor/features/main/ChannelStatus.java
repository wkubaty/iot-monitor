package com.example.wojciech.iotmonitor.features.main;

public class ChannelStatus {
    private String name;
    private String lastUpdate;

    public ChannelStatus(String name, String lastUpdate) {
        this.name = name;
        this.lastUpdate = lastUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
