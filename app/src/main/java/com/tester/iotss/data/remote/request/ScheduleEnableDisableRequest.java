package com.tester.iotss.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class ScheduleEnableDisableRequest {
    @SerializedName("id")
    private String id;
    @SerializedName("id_book")
    private String id_book;
    @SerializedName("is_active")
    private boolean is_active;

    public ScheduleEnableDisableRequest(String id, String id_book, boolean is_active) {
        this.id = id;
        this.id_book = id_book;
        this.is_active = is_active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_book() {
        return id_book;
    }

    public void setId_book(String id_book) {
        this.id_book = id_book;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
}
