package com.tester.iotss.data.remote.response;

import com.tester.iotss.data.model.Schedule;

import java.util.List;

public class ScheduleResponse {
    private int status;
    private String message;
    private List<Schedule> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Schedule> getData() {
        return data;
    }

    public void setData(List<Schedule> data) {
        this.data = data;
    }
}
