package com.tester.iotss.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class GetUserScheduleRequest {
    @SerializedName("no_hp")
    private String noHp;

    public GetUserScheduleRequest(String noHp) {
        this.noHp = noHp;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }
}
