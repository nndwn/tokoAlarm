package com.tester.iotss.domain.model;

import com.google.gson.annotations.SerializedName;

public class HistoryList{

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("noreff")
	private String noreff;

	@SerializedName("jumlah")
	private String jumlah;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("id_users")
	private String idUsers;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("saldo_awal")
	private String saldoAwal;

	@SerializedName("tipe")
	private String tipe;

	@SerializedName("saldo_akhir")
	private String saldoAkhir;

	@SerializedName("status")
	private String status;

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public void setNoreff(String noreff){
		this.noreff = noreff;
	}

	public String getNoreff(){
		return noreff;
	}

	public void setJumlah(String jumlah){
		this.jumlah = jumlah;
	}

	public String getJumlah(){
		return jumlah;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setIdUsers(String idUsers){
		this.idUsers = idUsers;
	}

	public String getIdUsers(){
		return idUsers;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSaldoAwal(String saldoAwal){
		this.saldoAwal = saldoAwal;
	}

	public String getSaldoAwal(){
		return saldoAwal;
	}

	public void setTipe(String tipe){
		this.tipe = tipe;
	}

	public String getTipe(){
		return tipe;
	}

	public void setSaldoAkhir(String saldoAkhir){
		this.saldoAkhir = saldoAkhir;
	}

	public String getSaldoAkhir(){
		return saldoAkhir;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}