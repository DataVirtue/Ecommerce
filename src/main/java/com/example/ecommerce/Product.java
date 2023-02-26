package com.example.ecommerce;

public class Product {
    private String name;
    private int id;

    private int quanitity;

    private double price;

    public Product(int id, String name , int quantity, double price ){
        this.id = id;
        this.name = name;
        this.quanitity = quantity;
        this.price = price;

    }

    // getters
    public String getName(){return this.name;}

    public int getId(){ return this.id;}

    public int getQuanitity(){return this.quanitity;}

    public double getPrice(){return this.price;}



}
