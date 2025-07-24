package com.example.order2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Map.Entry<Dish, Integer>> cartItems;
    private final CartUpdateListener listener;

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(Map<Dish, Integer> cart, CartUpdateListener listener) {
        this.cartItems = new ArrayList<>(cart.entrySet());
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Map.Entry<Dish, Integer> entry = cartItems.get(position);
        Dish dish = entry.getKey();
        int quantity = entry.getValue();

        holder.dishName.setText(dish.getName());
        holder.quantity.setText(String.valueOf(quantity));

        holder.increaseButton.setOnClickListener(v -> {
            int currentQuantity = CartManager.getInstance().getCart().get(dish);
            CartManager.getInstance().getCart().put(dish, currentQuantity + 1);
            notifyDataSetChanged();
            listener.onCartUpdated();
        });

        holder.decreaseButton.setOnClickListener(v -> {
            int currentQuantity = CartManager.getInstance().getCart().get(dish);
            if (currentQuantity > 1) {
                CartManager.getInstance().getCart().put(dish, currentQuantity - 1);
            } else {
                CartManager.getInstance().getCart().remove(dish);
                cartItems.remove(entry);
            }
            notifyDataSetChanged();
            listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView dishName;
        TextView quantity;
        ImageButton increaseButton;
        ImageButton decreaseButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.tvDishName);
            quantity = itemView.findViewById(R.id.tvQuantity);
            increaseButton = itemView.findViewById(R.id.btnIncrease);
            decreaseButton = itemView.findViewById(R.id.btnDecrease);
        }
    }
} 