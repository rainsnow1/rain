package com.example.order2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库名称
    private static final String DATABASE_NAME = "menu.db";
    // 数据库版本
    private static final int DATABASE_VERSION = 3;

    // 表名
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_ORDER_ITEMS = "order_items";

    // users表的列名
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_PASSWORD = "password";

    // orders表的列名
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_USERNAME = "order_username";
    public static final String COLUMN_TOTAL_PRICE = "total_price";
    public static final String COLUMN_ORDER_DATE = "order_date";

    // order_items表的列名
    public static final String COLUMN_ORDER_ITEM_ID = "order_item_id";
    public static final String COLUMN_DISH_NAME = "dish_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_PRICE = "price";

    // 创建users表的SQL语句
    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT UNIQUE, " +
            COLUMN_PHONE_NUMBER + " TEXT UNIQUE, " +
            COLUMN_PASSWORD + " TEXT);";

    // 创建orders表的SQL语句
    private static final String TABLE_CREATE_ORDERS =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
            COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ORDER_USERNAME + " TEXT, " +
            COLUMN_TOTAL_PRICE + " REAL, " +
            COLUMN_ORDER_DATE + " TEXT, " +
            "FOREIGN KEY(" + COLUMN_ORDER_USERNAME + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USERNAME + "));";

    // 创建order_items表的SQL语句
    private static final String TABLE_CREATE_ORDER_ITEMS =
            "CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
            COLUMN_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ORDER_ID + " INTEGER, " +
            COLUMN_DISH_NAME + " TEXT, " +
            COLUMN_QUANTITY + " INTEGER, " +
            COLUMN_PRICE + " REAL, " +
            "FOREIGN KEY(" + COLUMN_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ORDER_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_ORDERS);
        db.execSQL(TABLE_CREATE_ORDER_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // 从v1升级到v2: 创建orders和order_items表
            db.execSQL(TABLE_CREATE_ORDERS);
            db.execSQL(TABLE_CREATE_ORDER_ITEMS);
        }
        if (oldVersion < 3) {
            // 从v2升级到v3: 重建orders表以使用username
            // 注意: 这会删除所有旧的订单数据
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL(TABLE_CREATE_ORDERS);
            db.execSQL(TABLE_CREATE_ORDER_ITEMS);
        }
    }
} 