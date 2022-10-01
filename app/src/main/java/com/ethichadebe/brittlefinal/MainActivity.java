package com.ethichadebe.brittlefinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.ethichadebe.brittlefinal.adapters.ShopItemAdapter;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.viewmodel.ShopViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ShopViewModel shopViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new GridLayoutManager(this,2));
        rvItems.setHasFixedSize(true);


        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getShops().observe(this, new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shops) {
                final ShopItemAdapter adapter = new ShopItemAdapter(MainActivity.this,shops);
                rvItems.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "works", Toast.LENGTH_SHORT).show();
            }
        });

    }
}