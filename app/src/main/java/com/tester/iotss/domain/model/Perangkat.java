package com.tester.iotss.domain.model;

public class Perangkat {
    private int id;
    private String id_alat;
    private String nama_alat;
    private String tanggal_mulai;
    private String tanggal_selesai;
    private int sisa_hari;

    private String nama_sensor_1;

    private String nama_sensor_2;

    private String nama_sensor_3;

    public Perangkat() {
    }

    public Perangkat(String id, String id_alat, String nama_alat, String tanggal_mulai, String tanggal_selesai, int sisa_hari, String nama_sensor_1, String nama_sensor_2, String nama_sensor_3) {
        this.id = Integer.parseInt(id);
        this.id_alat = id_alat;
        this.nama_alat = nama_alat;
        this.tanggal_mulai = tanggal_mulai;
        this.tanggal_selesai = tanggal_selesai;
        this.sisa_hari = sisa_hari;
        this.nama_sensor_1 = nama_sensor_1;
        this.nama_sensor_2 = nama_sensor_2;
        this.nama_sensor_3 = nama_sensor_3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNama_sensor_1() {
        return nama_sensor_1;
    }

    public void setNama_sensor_1(String nama_sensor_1) {
        this.nama_sensor_1 = nama_sensor_1;
    }

    public String getNama_sensor_2() {
        return nama_sensor_2;
    }

    public void setNama_sensor_2(String nama_sensor_2) {
        this.nama_sensor_2 = nama_sensor_2;
    }

    public String getNama_sensor_3() {
        return nama_sensor_3;
    }

    public void setNama_sensor_3(String nama_sensor_3) {
        this.nama_sensor_3 = nama_sensor_3;
    }
}