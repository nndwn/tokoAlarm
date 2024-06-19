package com.tester.iotss.domain.model;

import org.json.JSONArray;

public class ListHistory {
    String id_transaksi;
    String invoice;
    String total_bayar;
    String total_item;
    String biaya_kirim;
    String alamat_kirim;
    String pengirim;
    String status_bayar;
    String tanggal;
    String id_payment;
    String gambar_payment;
    String pemilik_rekening;
    String nama_payment;
    String norek;
    String iscod;
    String kode_promo;
    String potongan;
    String status_kirim;
    String status_transaksi;
    String grandtotal;

    JSONArray dataitem;

    public JSONArray getDataitem() {
        return dataitem;
    }

    public void setDataitem(JSONArray dataitem) {
        this.dataitem = dataitem;
    }

    public String getGambar_payment() {
        return gambar_payment;
    }

    public void setGambar_payment(String gambar_payment) {
        this.gambar_payment = gambar_payment;
    }

    public String getPemilik_rekening() {
        return pemilik_rekening;
    }

    public void setPemilik_rekening(String pemilik_rekening) {
        this.pemilik_rekening = pemilik_rekening;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(String grandtotal) {
        this.grandtotal = grandtotal;
    }

    public String getStatus_transaksi() {
        return status_transaksi;
    }

    public void setStatus_transaksi(String status_transaksi) {
        this.status_transaksi = status_transaksi;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getTotal_bayar() {
        return total_bayar;
    }

    public void setTotal_bayar(String total_bayar) {
        this.total_bayar = total_bayar;
    }

    public String getTotal_item() {
        return total_item;
    }

    public void setTotal_item(String total_item) {
        this.total_item = total_item;
    }

    public String getBiaya_kirim() {
        return biaya_kirim;
    }

    public void setBiaya_kirim(String biaya_kirim) {
        this.biaya_kirim = biaya_kirim;
    }

    public String getAlamat_kirim() {
        return alamat_kirim;
    }

    public void setAlamat_kirim(String alamat_kirim) {
        this.alamat_kirim = alamat_kirim;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public String getStatus_bayar() {
        return status_bayar;
    }

    public void setStatus_bayar(String status_bayar) {
        this.status_bayar = status_bayar;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getId_payment() {
        return id_payment;
    }

    public void setId_payment(String id_payment) {
        this.id_payment = id_payment;
    }

    public String getNama_payment() {
        return nama_payment;
    }

    public void setNama_payment(String nama_payment) {
        this.nama_payment = nama_payment;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    public String getIscod() {
        return iscod;
    }

    public void setIscod(String iscod) {
        this.iscod = iscod;
    }

    public String getKode_promo() {
        return kode_promo;
    }

    public void setKode_promo(String kode_promo) {
        this.kode_promo = kode_promo;
    }

    public String getPotongan() {
        return potongan;
    }

    public void setPotongan(String potongan) {
        this.potongan = potongan;
    }

    public String getStatus_kirim() {
        return status_kirim;
    }

    public void setStatus_kirim(String status_kirim) {
        this.status_kirim = status_kirim;
    }
}
