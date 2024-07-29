package com.tester.iotss.domain.model;

public class ListTopUp {
    private int id;
    private int price;

    public void SetId(int id) {this.id = id;}
    public int GetId() {return id;}

    public void SetPrice(int price) {this.price = price;}
    public int GetPrice() {return price;}

    public ListTopUp(int id , int price)
    {
        this.id = id;
        this.price = price;
    }
}
