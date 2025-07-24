package com.example.order2;

import java.util.List;

public class Order {
    private long id;
    private String username;
    private String orderDate;
    private double totalPrice;
    private List<OrderItem> items;

    public Order(long id, String username, String orderDate, double totalPrice, List<OrderItem> items) {
        this.id = id;
        this.username = username;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getOrderDate() { return orderDate; }
    public double getTotalPrice() { return totalPrice; }
    public List<OrderItem> getItems() { return items; }
} 