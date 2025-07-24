package com.example.order2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        // 获取视图引用
        TextInputEditText usernameEditText = findViewById(R.id.etUsername);
        TextInputEditText passwordEditText = findViewById(R.id.etPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        Button registerButton = findViewById(R.id.btnRegister);

        // 设置登录按钮点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameOrPhone = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // 简单验证
                if (usernameOrPhone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "请填写用户名和密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 数据库验证
                String username = checkUser(usernameOrPhone, password);
                if (username != null) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    UserManager.getInstance().setCurrentUsername(username);

                    if (username.equals("chef")) {
                        // 跳转到厨师端界面
                        Intent intent = new Intent(LoginActivity.this, ChefMainActivity.class);
                        startActivity(intent);
                    } else {
                        // 跳转到客户端主界面
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 设置注册按钮点击事件
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到注册活动
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private String checkUser(String usernameOrPhone, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "(" + DatabaseHelper.COLUMN_USERNAME + " = ? OR " + DatabaseHelper.COLUMN_PHONE_NUMBER + " = ?) AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {usernameOrPhone, usernameOrPhone, password};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String username = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
        }
        cursor.close();
        db.close();
        return username;
    }
}