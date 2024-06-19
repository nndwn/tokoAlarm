package com.tester.iotss.domain.model;

import com.google.gson.annotations.SerializedName;

public class ListHistoryInput {

	@SerializedName("jenis_paslon")
	private String jenisPaslon;

	@SerializedName("provinsi")
	private String provinsi;

	@SerializedName("misi")
	private String misi;

	@SerializedName("multi_dapil")
	private String multiDapil;

	@SerializedName("images")
	private String images;

	@SerializedName("tingkatan")
	private String tingkatan;

	@SerializedName("nama_paslon")
	private String namaPaslon;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("kabupaten")
	private String kabupaten;

	@SerializedName("kelurahan")
	private String kelurahan;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("jumlah_kursi")
	private String jumlahKursi;

	@SerializedName("suara_sah")
	private String suaraSah;

	@SerializedName("nama_dapil")
	private String namaDapil;

	@SerializedName("suara_tidaksah")
	private String suaraTidaksah;

	@SerializedName("id")
	private String id;

	@SerializedName("visi")
	private String visi;

	public void setJenisPaslon(String jenisPaslon){
		this.jenisPaslon = jenisPaslon;
	}

	public String getJenisPaslon(){
		return jenisPaslon;
	}

	public void setProvinsi(String provinsi){
		this.provinsi = provinsi;
	}

	public String getProvinsi(){
		return provinsi;
	}

	public void setMisi(String misi){
		this.misi = misi;
	}

	public String getMisi(){
		return misi;
	}

	public void setMultiDapil(String multiDapil){
		this.multiDapil = multiDapil;
	}

	public String getMultiDapil(){
		return multiDapil;
	}

	public void setImages(String images){
		this.images = images;
	}

	public String getImages(){
		return images;
	}

	public void setTingkatan(String tingkatan){
		this.tingkatan = tingkatan;
	}

	public String getTingkatan(){
		return tingkatan;
	}

	public void setNamaPaslon(String namaPaslon){
		this.namaPaslon = namaPaslon;
	}

	public String getNamaPaslon(){
		return namaPaslon;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setKabupaten(String kabupaten){
		this.kabupaten = kabupaten;
	}

	public String getKabupaten(){
		return kabupaten;
	}

	public void setKelurahan(String kelurahan){
		this.kelurahan = kelurahan;
	}

	public String getKelurahan(){
		return kelurahan;
	}

	public void setKecamatan(String kecamatan){
		this.kecamatan = kecamatan;
	}

	public String getKecamatan(){
		return kecamatan;
	}

	public void setJumlahKursi(String jumlahKursi){
		this.jumlahKursi = jumlahKursi;
	}

	public String getJumlahKursi(){
		return jumlahKursi;
	}

	public void setSuaraSah(String suaraSah){
		this.suaraSah = suaraSah;
	}

	public String getSuaraSah(){
		return suaraSah;
	}

	public void setNamaDapil(String namaDapil){
		this.namaDapil = namaDapil;
	}

	public String getNamaDapil(){
		return namaDapil;
	}

	public void setSuaraTidaksah(String suaraTidaksah){
		this.suaraTidaksah = suaraTidaksah;
	}

	public String getSuaraTidaksah(){
		return suaraTidaksah;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setVisi(String visi){
		this.visi = visi;
	}

	public String getVisi(){
		return visi;
	}
}