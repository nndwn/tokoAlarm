package com.tester.iotss.Model;

import com.google.gson.annotations.SerializedName;

public class ListPromo{

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("banner")
    private String banner;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private String id;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("title")
    private String title;

    public void setUpdatedAt(String updatedAt){
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt(){
        return updatedAt;
    }

    public void setBanner(String banner){
        this.banner = banner;
    }

    public String getBanner(){
        return banner;
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

    public void setDeskripsi(String deskripsi){
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi(){
        return deskripsi;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}