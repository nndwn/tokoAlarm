package com.tester.iotss.Data.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.tester.iotss.Data.Database.AlatDao;

@Entity(tableName = "alat")
public class Alat{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("id_reference")
    private String id_reference;

    @SerializedName("nama_paket")
    private String namaPaket;

    @SerializedName("nomor_paket")
    private String nomorPaket;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("biaya_rupiah")
    private String biayaRupiah;

    @SerializedName("cutoff_day")
    private String cutoffDay;

    @SerializedName("periode")
    private String periode;

    @SerializedName("tanggal_selesai")
    private String tanggalSelesai;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("biaya")
    private String biaya;

    @SerializedName("tanggal_mulai")
    private String tanggalMulai;

    @SerializedName("id_alat")
    private String idAlat;

    @SerializedName("nohp")
    private String nohp;

    @SerializedName("sisa_hari")
    private String sisaHari;

    @SerializedName("status")
    private String status;

    @SerializedName("day_convertion")
    private String dayConvertion;

    public Alat(){}


    @Ignore
    public Alat(int id, String id_reference, String namaPaket, String nomorPaket, String createdAt, String biayaRupiah, String cutoffDay, String periode, String tanggalSelesai, String updatedAt, String biaya, String tanggalMulai, String idAlat, String nohp, String sisaHari, String status, String dayConvertion) {
        this.id = id;
        this.id_reference = id_reference;
        this.namaPaket = namaPaket;
        this.nomorPaket = nomorPaket;
        this.createdAt = createdAt;
        this.biayaRupiah = biayaRupiah;
        this.cutoffDay = cutoffDay;
        this.periode = periode;
        this.tanggalSelesai = tanggalSelesai;
        this.updatedAt = updatedAt;
        this.biaya = biaya;
        this.tanggalMulai = tanggalMulai;
        this.idAlat = idAlat;
        this.nohp = nohp;
        this.sisaHari = sisaHari;
        this.status = status;
        this.dayConvertion = dayConvertion;
    }

    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getId_reference() {
        return id_reference;
    }

    public void setId_reference(String id_reference) {
        this.id_reference = id_reference;
    }

    public void setNomorPaket(String nomorPaket){
        this.nomorPaket = nomorPaket;
    }

    public String getNomorPaket(){
        return nomorPaket;
    }

    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    public String getCreatedAt(){
        return createdAt;
    }

    public void setBiayaRupiah(String biayaRupiah){
        this.biayaRupiah = biayaRupiah;
    }

    public String getBiayaRupiah(){
        return biayaRupiah;
    }

    public void setCutoffDay(String cutoffDay){
        this.cutoffDay = cutoffDay;
    }

    public String getCutoffDay(){
        return cutoffDay;
    }

    public void setPeriode(String periode){
        this.periode = periode;
    }

    public String getPeriode(){
        return periode;
    }

    public void setTanggalSelesai(String tanggalSelesai){
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getTanggalSelesai(){
        return tanggalSelesai;
    }

    public void setUpdatedAt(String updatedAt){
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt(){
        return updatedAt;
    }

    public void setBiaya(String biaya){
        this.biaya = biaya;
    }

    public String getBiaya(){
        return biaya;
    }

    public void setTanggalMulai(String tanggalMulai){
        this.tanggalMulai = tanggalMulai;
    }

    public String getTanggalMulai(){
        return tanggalMulai;
    }

    public void setIdAlat(String idAlat){
        this.idAlat = idAlat;
    }

    public String getIdAlat(){
        return idAlat;
    }

    public void setNohp(String nohp){
        this.nohp = nohp;
    }

    public String getNohp(){
        return nohp;
    }

    public void setSisaHari(String sisaHari){
        this.sisaHari = sisaHari;
    }

    public String getSisaHari(){
        return sisaHari;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public void setDayConvertion(String dayConvertion){
        this.dayConvertion = dayConvertion;
    }

    public String getDayConvertion(){
        return dayConvertion;
    }


    public String getNamaPaket() {
        return namaPaket;
    }

    public void setNamaPaket(String namaPaket) {
        this.namaPaket = namaPaket;
    }
}
