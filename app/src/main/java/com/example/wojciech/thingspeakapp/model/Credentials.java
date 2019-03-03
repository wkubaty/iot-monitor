package com.example.wojciech.thingspeakapp.model;

import java.io.Serializable;

public class Credentials implements Serializable {
    private int id;
    private String name;
    private String apiKey;

    public Credentials(int id, String name, String apiKey) {
        this.id = id;
        this.name = name;
        this.apiKey = apiKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
