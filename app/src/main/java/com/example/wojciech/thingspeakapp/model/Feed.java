package com.example.wojciech.thingspeakapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Feed implements Serializable {
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("entry_id")
    private int entryId;
    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;
    private String field6;
    private String field7;
    private String field8;
    private String[] fields;

    public String getCreatedAt() {
        return createdAt;
    }

    public int getEntryId() {
        return entryId;
    }

    public String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public String getField3() {
        return field3;
    }

    public String getField4() {
        return field4;
    }

    public String getField5() {
        return field5;
    }

    public String getField6() {
        return field6;
    }

    public String getField7() {
        return field7;
    }

    public String getField8() {
        return field8;
    }

    public String[] getFields() {
        if(fields == null){
            fields = new String[8];
            fields[0] = field1;
            fields[1] = field2;
            fields[2] = field3;
            fields[3] = field4;
            fields[4] = field5;
            fields[5] = field6;
            fields[6] = field7;
            fields[7] = field8;
        }
        return fields;
    }
}
