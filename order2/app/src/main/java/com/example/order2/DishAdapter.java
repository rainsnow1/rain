package com.example.order2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private List<Dish> dishList;

    public DishAdapter(List<Dish> dishList) {
        this.dishList = dishList;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.dishName.setText(dish.getName());
        holder.dishDescription.setText(dish.getDescription());
        holder.dishPrice.setText(String.format(Locale.getDefault(), "￥%.2f", dish.getPrice()));
        holder.dishImage.setImageResource(dish.getImageResId());

        holder.addToCartButton.setOnClickListener(v -> {
            CartManager.getInstance().addDish(dish);
            Toast.makeText(v.getContext(), dish.getName() + " 已加入购物车", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DishDetailActivity.class);
            intent.putExtra(DishDetailActivity.EXTRA_DISH, dish);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public void filterList(List<Dish> filteredList) {
        dishList = filteredList;
        notifyDataSetChanged();
    }

    static class DishViewHolder extends RecyclerView.ViewHolder {
        ImageView dishImage;
        TextView dishName;
        TextView dishDescription;
        TextView dishPrice;
        Button addToCartButton;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.ivDishImage);
            dishName = itemView.findViewById(R.id.tvDishName);
            dishDescription = itemView.findViewById(R.id.tvDishDescription);
            dishPrice = itemView.findViewById(R.id.tvDishPrice);
            addToCartButton = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}