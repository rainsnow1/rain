package com.example.order2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChefMainActivity extends AppCompatActivity implements OrderAdapter.OrderCompleteListener {

    private DatabaseHelper dbHelper;
    private RecyclerView rvOrders;
    private OrderAdapter adapter;
    private List<Order> orderList;
    private TextView tvEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_main);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        tvEmptyState = findViewById(R.id.tvEmptyState);

        loadOrders();
    }

    private void loadOrders() {
        orderList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 1. 查询所有订单
        Cursor orderCursor = db.query(DatabaseHelper.TABLE_ORDERS, null, null, null, null, null, DatabaseHelper.COLUMN_ORDER_DATE + " DESC");

        while (orderCursor.moveToNext()) {
            long orderId = orderCursor.getLong(orderCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID));
            String username = orderCursor.getString(orderCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_USERNAME));
            String orderDate = orderCursor.getString(orderCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DATE));
            double totalPrice = orderCursor.getDouble(orderCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOTAL_PRICE));

            // 2. 根据orderId查询该订单的所有订单项
            List<OrderItem> orderItems = new ArrayList<>();
            Cursor itemCursor = db.query(DatabaseHelper.TABLE_ORDER_ITEMS,
                    null,
                    DatabaseHelper.COLUMN_ORDER_ID + " = ?",
                    new String[]{String.valueOf(orderId)},
                    null, null, null);

            while (itemCursor.moveToNext()) {
                String dishName = itemCursor.getString(itemCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISH_NAME));
                int quantity = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY));
                double price = itemCursor.getDouble(itemCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
                orderItems.add(new OrderItem(dishName, quantity, price));
            }
            itemCursor.close();

            orderList.add(new Order(orderId, username, orderDate, totalPrice, orderItems));
        }
        orderCursor.close();
        db.close();

        adapter = new OrderAdapter(orderList, this);
        rvOrders.setAdapter(adapter);

        checkEmptyState();
    }

    @Override
    public void onOrderComplete(Order order) {
        // 从数据库中删除订单
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // 先删除order_items
            db.delete(DatabaseHelper.TABLE_ORDER_ITEMS, DatabaseHelper.COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(order.getId())});
            // 再删除orders
            db.delete(DatabaseHelper.TABLE_ORDERS, DatabaseHelper.COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(order.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        // 从UI中移除
        int position = orderList.indexOf(order);
        if (position != -1) {
            orderList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, orderList.size());
            Toast.makeText(this, "订单 #" + order.getId() + " 已完成", Toast.LENGTH_SHORT).show();
            checkEmptyState();
        }
    }

    private void checkEmptyState() {
        if (orderList.isEmpty()) {
            rvOrders.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvOrders.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
        }
    }
} 