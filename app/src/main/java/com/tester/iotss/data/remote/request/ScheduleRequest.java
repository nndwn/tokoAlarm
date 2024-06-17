package com.tester.iotss.data.remote.request;

public class ScheduleRequest {
    public int device;
    public String name;
    public String start;
    public String end;
    public String days;
    public int is_active;
    public int sensor_switch;
    public int sensor_ohm;
    public int sensor_rf;

    public ScheduleRequest(int device,String name, String start, String end, String days, int is_active, int sensor_switch, int sensor_ohm, int sensor_rf) {
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
}
