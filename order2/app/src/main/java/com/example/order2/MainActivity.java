package com.example.order2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private List<Dish> allDishes;
    private DishAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("菜单");

        setupDishes();
        setupRecyclerView();
        setupTabLayout();

        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void setupDishes() {
        allDishes = new ArrayList<>();
        allDishes.add(new Dish("宫保鸡丁", "经典川菜，麻辣鲜香，入口咸鲜，回味略甜。", 38.00, R.drawable.dish1, "川菜", "鸡丁、花生、干辣椒", "约250克", "葱、姜、蒜"));
        allDishes.add(new Dish("鱼香肉丝", "鱼香味型，此菜无鱼，但有鱼香，酸甜可口。", 32.00, R.drawable.dish2, "川菜", "猪肉丝、木耳、笋丝", "约220克", "泡椒、葱、姜、蒜"));
        allDishes.add(new Dish("水煮牛肉", "麻辣味厚，肉质滑嫩，是下饭的绝佳选择。", 58.00, R.drawable.dish3, "川菜", "牛肉片、豆芽、青菜", "约300克", "花椒、干辣椒、豆瓣酱"));
        allDishes.add(new Dish("麻婆豆腐", "麻辣烫，豆腐嫩滑，口感丰富。", 22.00, R.drawable.dish4, "家常菜", "豆腐、牛肉末", "约200克", "豆瓣酱、花椒粉、葱花"));
        allDishes.add(new Dish("清炒时蔬", "健康素食，选用当季新鲜蔬菜，清淡爽口。", 18.00, R.drawable.dish5, "素食", "时令蔬菜", "约200克", "蒜蓉"));
        allDishes.add(new Dish("玉米排骨汤", "开胃汤品，汤汁鲜美，玉米甘甜，排骨软糯。", 16.00, R.drawable.dish6, "汤品", "排骨、玉米", "约350克", "胡萝卜、姜片"));
        allDishes.add(new Dish("白切鸡", "皮爽肉滑，清淡鲜美，配以秘制蘸料，风味独特。", 78.00, R.drawable.dish7, "粤菜", "三黄鸡", "半只", "姜蓉、葱油"));
        allDishes.add(new Dish("三鲜汤", "味香甜美，清淡爽口，营养丰富。", 52.00, R.drawable.dish8, "汤品", "虾仁、肉丸、青菜", "约300克", "菌菇、葱花"));
        allDishes.add(new Dish("酸辣鲈鱼", "酸甜可口，清淡不腻，鱼肉鲜嫩无刺。", 66.00, R.drawable.dish9, "粤菜", "鲈鱼", "一条", "番茄、酸菜、辣椒"));
        allDishes.add(new Dish("糖醋排骨", "酸甜可口，肉质鲜嫩，色泽红亮诱人。", 42.00, R.drawable.dish10, "家常菜", "猪排骨", "约280克", "冰糖、醋、芝麻"));
        allDishes.add(new Dish("肉沫茄子", "滑腻爽口，下饭神器，咸香适中。", 32.00, R.drawable.dish11, "家常菜", "茄子、猪肉末", "约250克", "蒜、豆瓣酱"));
        allDishes.add(new Dish("土豆丝", "清清甜甜，清淡爽口，家常小炒的经典之作。", 17.00, R.drawable.dish12, "素食", "土豆", "约180克", "青椒、红椒"));
    }

    private void setupRecyclerView() {
        RecyclerView rvDishes = findViewById(R.id.rvDishes);
        rvDishes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DishAdapter(new ArrayList<>(allDishes));
        rvDishes.setAdapter(adapter);
    }

    private void setupTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // 获取所有分类
        Set<String> categories = new LinkedHashSet<>();
        categories.add("全部");
        for (Dish dish : allDishes) {
            categories.add(dish.getCategory());
        }

        // 添加Tab
        for (String category : categories) {
            tabLayout.addTab(tabLayout.newTab().setText(category));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedCategory = tab.getText().toString();
                filterDishes(selectedCategory);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void filterDishes(String category) {
        if (category.equals("全部")) {
            adapter.filterList(new ArrayList<>(allDishes));
        } else {
            List<Dish> filteredDishes = allDishes.stream()
                    .filter(dish -> dish.getCategory().equals(category))
                    .collect(Collectors.toList());
            adapter.filterList(filteredDishes);
        }
    }
}