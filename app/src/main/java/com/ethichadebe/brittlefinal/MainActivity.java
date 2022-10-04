package com.ethichadebe.brittlefinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.ethichadebe.brittlefinal.adapters.ShopItemAdapter;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.viewmodel.ShopViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ShopViewModel shopViewModel;
    private List<Shop> shops = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setHasFixedSize(true);
        final ShopItemAdapter adapter = new ShopItemAdapter();
        rvItems.setAdapter(adapter);

        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getShops().observe(this, new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shops) {
                adapter.setShopAdapter(MainActivity.this, shops);
                Toast.makeText(MainActivity.this, "OnChanged ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}