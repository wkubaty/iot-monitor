package com.example.wojciech.iotmonitor.model.thingspeak;

import java.io.Serializable;
import java.util.Objects;

public class Credentials implements Serializable {
    private int id;
    private String name;
    private String apiKey;

    public Credentials(int id, String apiKey) {
        this.id = id;
        this.name="";
        this.apiKey = apiKey;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credentials that = (Credentials) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(apiKey, that.apiKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, apiKey);
    }
}
