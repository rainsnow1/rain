package com.example.order2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartManager {

    private static CartManager instance;
    private final Map<Dish, Integer> cart;

    private CartManager() {
        cart = new LinkedHashMap<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addDish(Dish dish) {
        if (cart.containsKey(dish)) {
            cart.put(dish, cart.get(dish) + 1);
        } else {
            cart.put(dish, 1);
        }
    }

    public Map<Dish, Integer> getCart() {
        return cart;
    }

    public double getTotalPrice() {
        double total = 0;
        for (Map.Entry<Dish, Integer> entry : cart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public void clearCart() {
        cart.clear();
    }
    
    public int getItemsCount() {
        return cart.size();
    }
} 