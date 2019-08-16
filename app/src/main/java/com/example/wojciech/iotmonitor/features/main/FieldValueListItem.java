package com.example.wojciech.iotmonitor.features.main;

public class FieldValueListItem {
    private String field;
    private String value;

    public FieldValueListItem(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
