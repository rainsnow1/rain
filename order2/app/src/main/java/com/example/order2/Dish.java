package com.example.order2;

import java.io.Serializable;

public class Dish implements Serializable {
    private String name;
    private String description;
    private double price;
    private int imageResId;
    private String category;
    private String mainIngredients;
    private String portion;
    private String sideIngredients;

    public Dish(String name, String description, double price, int imageResId, String category, String mainIngredients, String portion, String sideIngredients) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
        this.category = category;
        this.mainIngredients = mainIngredients;
        this.portion = portion;
        this.sideIngredients = sideIngredients;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getCategory() {
        return category;
    }

    public String getMainIngredients() {
        return mainIngredients;
    }

    public String getPortion() {
        return portion;
    }

    public String getSideIngredients() {
        return sideIngredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return name.equals(dish.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}