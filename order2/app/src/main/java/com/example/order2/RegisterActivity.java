package com.example.order2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextInputEditText usernameEditText = findViewById(R.id.etUsername);
        TextInputEditText phoneNumberEditText = findViewById(R.id.etPhoneNumber);
        TextInputEditText passwordEditText = findViewById(R.id.etPassword);
        Button registerButton = findViewById(R.id.btnRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isUserExists(username, phoneNumber)) {
                    Toast.makeText(RegisterActivity.this, "用户名或手机号已存在", Toast.LENGTH_SHORT).show();
                } else {
                    addUser(username, phoneNumber, password);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish(); // 注册成功后关闭当前活动
                }
            }
        });
    }

    private boolean isUserExists(String username, String phoneNumber) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DatabaseHelper.COLUMN_USERNAME + " = ? OR " + DatabaseHelper.COLUMN_PHONE_NUMBER + " = ?";
        String[] selectionArgs = { username, phoneNumber };
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                null, // 查询所有列
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    private void addUser(String username, String phoneNumber, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password); // 注意：实际应用中密码应该加密存储
        db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
    }
} 