package com.tester.iotss.domain.model;

public class PaymentMetode {
    private int id ;
    private String name;
    private long  nomor;
    private String pemilik;


    public void SetId(int id){this.id = id; }
    public int GetId() {return id;}

    public void SetName(String name){this.name = name;}
    public String GetName() {return name;}

    public void SetNomor(int nomor){this.nomor = nomor;}
    public long GetNomor() {return nomor;}

    public void SetPemilik(String pemilik){this.pemilik = pemilik;}
    public String GetPemilik() {return pemilik;}

    public PaymentMetode(int id , String name , int nomor , String pemilik) {
        this.id = id;
        this.name = name;
        this.nomor = nomor;
    }
}
