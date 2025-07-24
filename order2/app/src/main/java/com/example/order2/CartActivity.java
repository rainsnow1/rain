package com.example.order2;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartUpdateListener {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private Button btnSubmitOrder;
    private CartAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvCartItems = findViewById(R.id.rvCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
        updateTotalPrice();

        btnSubmitOrder.setOnClickListener(v -> submitOrder());
    }

    private void setupAdapter() {
        adapter = new CartAdapter(CartManager.getInstance().getCart(), this);
        rvCartItems.setAdapter(adapter);
    }

    @Override
    public void onCartUpdated() {
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = CartManager.getInstance().getTotalPrice();
        tvTotalPrice.setText(String.format(Locale.getDefault(), "总计: ￥%.2f", totalPrice));
    }

    private void submitOrder() {
        CartManager cartManager = CartManager.getInstance();
        if (cartManager.getCart().isEmpty()) {
            Toast.makeText(this, "购物车是空的", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = UserManager.getInstance().getCurrentUsername();
        if (username == null) {
            Toast.makeText(this, "用户未登录，无法提交订单", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // 1. 插入到orders表
            ContentValues orderValues = new ContentValues();
            orderValues.put(DatabaseHelper.COLUMN_ORDER_USERNAME, username);
            orderValues.put(DatabaseHelper.COLUMN_TOTAL_PRICE, cartManager.getTotalPrice());
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            orderValues.put(DatabaseHelper.COLUMN_ORDER_DATE, currentDate);

            long orderId = db.insert(DatabaseHelper.TABLE_ORDERS, null, orderValues);

            // 2. 插入到order_items表
            for (Map.Entry<Dish, Integer> entry : cartManager.getCart().entrySet()) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(DatabaseHelper.COLUMN_ORDER_ID, orderId);
                itemValues.put(DatabaseHelper.COLUMN_DISH_NAME, entry.getKey().getName());
                itemValues.put(DatabaseHelper.COLUMN_QUANTITY, entry.getValue());
                itemValues.put(DatabaseHelper.COLUMN_PRICE, entry.getKey().getPrice());
                db.insert(DatabaseHelper.TABLE_ORDER_ITEMS, null, itemValues);
            }

            db.setTransactionSuccessful();
            Toast.makeText(this, "订单提交成功！", Toast.LENGTH_SHORT).show();
            cartManager.clearCart();
            finish();

        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 