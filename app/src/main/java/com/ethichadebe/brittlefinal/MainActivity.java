package com.ethichadebe.brittlefinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ethichadebe.brittlefinal.viewmodel.ShopViewModel;

public class MainActivity extends AppCompatActivity {
    ShopViewModel shopViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView rvItems = findViewById(R.id.rvItems);

    }
}