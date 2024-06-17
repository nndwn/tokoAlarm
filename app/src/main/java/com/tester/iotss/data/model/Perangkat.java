package com.tester.iotss.data.model;

import java.util.List;

public class Perangkat {
    private String id_alat;
    private String nama_alat;
    private String tanggal_mulai;
    private String tanggal_selesai;
    private int sisa_hari;
    private List<Sensor> sensor;

    public Perangkat() {
    }

    public Perangkat(String id_alat, String nama_alat, String tanggal_mulai, String tanggal_selesai, int sisa_hari, List<Sensor> sensor) {
        this.id_alat = id_alat;
        this.nama_alat = nama_alat;
        this.tanggal_mulai = tanggal_mulai;
        this.tanggal_selesai = tanggal_selesai;
        this.sisa_hari = sisa_hari;
        this.sensor = sensor;
    }

    public void setId_alat(String id_alat) {
        this.id_alat = id_alat;
    }

    public void setNama_alat(String nama_alat) {
        this.nama_alat = nama_alat;
    }

    public void setTanggal_mulai(String tanggal_mulai) {
        this.tanggal_mulai = tanggal_mulai;
    }

    public void setTanggal_selesai(String tanggal_selesai) {
        this.tanggal_selesai = tanggal_selesai;
    }

    public void setSisa_hari(int sisa_hari) {
        this.sisa_hari = sisa_hari;
    }

    public void setSensor(List<Sensor> sensor) {
        this.sensor = sensor;
    }

    public String getId_alat() {
        return id_alat;
    }

    public String getNama_alat() {
        return nama_alat;
    }

    public String getTanggal_mulai() {
        return tanggal_mulai;
    }

    public String getTanggal_selesai() {
        return tanggal_selesai;
    }

    public int getSisa_hari() {
        return sisa_hari;
    }

    public List<Sensor> getSensor() {
        return sensor;
    }
}