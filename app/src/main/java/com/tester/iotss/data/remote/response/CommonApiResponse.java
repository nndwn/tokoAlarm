package com.tester.iotss.data.remote.response;

import com.tester.iotss.data.model.Perangkat;

import java.util.List;

public class CommonApiResponse {
    private int status;
    private String message;
    private String data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
