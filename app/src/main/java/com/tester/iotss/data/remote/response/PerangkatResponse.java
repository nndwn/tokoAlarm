package com.tester.iotss.data.remote.response;

import com.tester.iotss.data.model.Perangkat;

import java.util.List;

public class PerangkatResponse {
    private int status;
    private String message;
    private List<Perangkat> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Perangkat> getData() {
        return data;
    }
}
