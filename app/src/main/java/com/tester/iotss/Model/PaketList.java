package com.tester.iotss.Model;

import com.google.gson.annotations.SerializedName;

public class PaketList{

	@SerializedName("biaya")
	private String biaya;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("biaya_rupiah")
	private String biayaRupiah;

	@SerializedName("id")
	private String id;

	@SerializedName("cutoff_day")
	private String cutoffDay;

	@SerializedName("periode")
	private String periode;

	@SerializedName("day_convertion")
	private String dayConvertion;

	public void setBiaya(String biaya){
		this.biaya = biaya;
	}

	public String getBiaya(){
		return biaya;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
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

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
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

	public void setDayConvertion(String dayConvertion){
		this.dayConvertion = dayConvertion;
	}

	public String getDayConvertion(){
		return dayConvertion;
	}
}