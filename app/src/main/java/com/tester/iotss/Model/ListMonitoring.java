package com.tester.iotss.Model;

import com.google.gson.annotations.SerializedName;

public class ListMonitoring{

	@SerializedName("tidaksah")
	private String tidaksah;

	@SerializedName("suarasah")
	private String suarasah;

	@SerializedName("wilayah")
	private String wilayah;

	public void setTidaksah(String tidaksah){
		this.tidaksah = tidaksah;
	}

	public String getTidaksah(){
		return tidaksah;
	}

	public void setSuarasah(String suarasah){
		this.suarasah = suarasah;
	}

	public String getSuarasah(){
		return suarasah;
	}

	public void setWilayah(String wilayah){
		this.wilayah = wilayah;
	}

	public String getWilayah(){
		return wilayah;
	}
}