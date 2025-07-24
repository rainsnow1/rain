package com.example.order2;

public class OrderItem {
    private String dishName;
    private int quantity;
    private double price;

    public OrderItem(String dishName, int quantity, double price) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getDishName() { return dishName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
} 