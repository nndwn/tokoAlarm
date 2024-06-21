package com.tester.iotss.data.remote.request;

public class ScheduleRequest {
    public String id;
    public int device;
    public String name;
    public String start;
    public String end;
    public String days;
    public int is_active;
    public int sensor_switch;
    public int sensor_ohm;
    public int sensor_rf;

    public ScheduleRequest() {
    }

    public ScheduleRequest(int device, String name, String start, String end, String days, int is_active, int sensor_switch, int sensor_ohm, int sensor_rf) {
        this.device = device;
        this.name = name;
        this.start = start;
        this.end = end;
        this.days = days;
        this.is_active = is_active;
        this.sensor_switch = sensor_switch;
        this.sensor_ohm = sensor_ohm;
        this.sensor_rf = sensor_rf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getSensor_switch() {
        return sensor_switch;
    }

    public void setSensor_switch(int sensor_switch) {
        this.sensor_switch = sensor_switch;
    }

    public int getSensor_ohm() {
        return sensor_ohm;
    }

    public void setSensor_ohm(int sensor_ohm) {
        this.sensor_ohm = sensor_ohm;
    }

    public int getSensor_rf() {
        return sensor_rf;
    }

    public void setSensor_rf(int sensor_rf) {
        this.sensor_rf = sensor_rf;
    }
}
