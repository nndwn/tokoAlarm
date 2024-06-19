package com.tester.iotss.domain.model;

public class ListPayment {
    String id_payment;
    String gambar;
    String nama_payment;
    String is_cod;
    String norek;
    String pemilik;

    public String getId_payment() {
        return id_payment;
    }

    public void setId_payment(String id_payment) {
        this.id_payment = id_payment;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNama_payment() {
        return nama_payment;
    }

    public void setNama_payment(String nama_payment) {
        this.nama_payment = nama_payment;
    }

    public String getIs_cod() {
        return is_cod;
    }

    public void setIs_cod(String is_cod) {
        this.is_cod = is_cod;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }
}
