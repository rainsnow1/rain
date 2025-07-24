package com.example.order2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class DishDetailActivity extends AppCompatActivity {

    public static final String EXTRA_DISH = "extra_dish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        final Dish dish = (Dish) getIntent().getSerializableExtra(EXTRA_DISH);
        if (dish == null) {
            Toast.makeText(this, "菜品信息错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(dish.getName());

        ImageView dishImage = findViewById(R.id.ivDishDetailImage);
        dishImage.setImageResource(dish.getImageResId());

        TextView dishPrice = findViewById(R.id.tvDishDetailPrice);
        dishPrice.setText(String.format(Locale.getDefault(), "￥%.2f", dish.getPrice()));

        TextView mainIngredients = findViewById(R.id.tvMainIngredients);
        mainIngredients.setText(dish.getMainIngredients());

        TextView portion = findViewById(R.id.tvPortion);
        portion.setText(dish.getPortion());

        TextView sideIngredients = findViewById(R.id.tvSideIngredients);
        sideIngredients.setText(dish.getSideIngredients());

        TextView dishDescription = findViewById(R.id.tvDishDetailDescription);
        dishDescription.setText(dish.getDescription());

        FloatingActionButton fab = findViewById(R.id.fabAddToCart);
        fab.setOnClickListener(view -> {
            CartManager.getInstance().addDish(dish);
            Toast.makeText(this, dish.getName() + " 已加入购物车", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 