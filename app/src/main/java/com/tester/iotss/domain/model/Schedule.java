package com.tester.iotss.domain.model;

import java.io.Serializable;

public class Schedule implements Serializable {
    private String id;

    private String id_book;

    private String name;
    private String start_time;
    private String end_time;
    private String days;
    private String is_active;
    private String sensor_switch;
    private String sensor_ohm;
    private String sensor_rf;
    private String nama_paket;
    private String id_alat;

    public Schedule() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getSensor_switch() {
        return sensor_switch;
    }

    public void setSensor_switch(String sensor_switch) {
        this.sensor_switch = sensor_switch;
    }

    public String getSensor_ohm() {
        return sensor_ohm;
    }

    public void setSensor_ohm(String sensor_ohm) {
        this.sensor_ohm = sensor_ohm;
    }

    public String getSensor_rf() {
        return sensor_rf;
    }

    public void setSensor_rf(String sensor_rf) {
        this.sensor_rf = sensor_rf;
    }

    public String getNama_paket() {
        return nama_paket;
    }

    public void setNama_paket(String nama_paket) {
        this.nama_paket = nama_paket;
    }

    public String getId_alat() {
        return id_alat;
    }

    public void setId_alat(String id_alat) {
        this.id_alat = id_alat;
    }

    public String getId_book() {
        return id_book;
    }

    public void setId_book(String id_book) {
        this.id_book = id_book;
    }
}
