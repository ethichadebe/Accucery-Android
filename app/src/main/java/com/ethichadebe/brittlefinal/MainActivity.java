package com.ethichadebe.brittlefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ethichadebe.brittlefinal.adapters.GroceryItemAdapter;
import com.ethichadebe.brittlefinal.adapters.ShopItemAdapter;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.viewmodel.GroceryItemViewModel;
import com.ethichadebe.brittlefinal.viewmodel.ShopViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private List<Shop> shops = new ArrayList<>();

    private ShopViewModel shopViewModel;

    private RecyclerView rvShops;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvShops = findViewById(R.id.rvShops);
        rvShops.setLayoutManager(new GridLayoutManager(this, 2));
        rvShops.setHasFixedSize(true);
        final ShopItemAdapter shopItemAdapter = new ShopItemAdapter();
        rvShops.setAdapter(shopItemAdapter);

        shopItemAdapter.setOnItemClickListener(position -> {
            if (shops.get(position).isActive()) {
                Intent intent = new Intent(MainActivity.this, GroceryListActivity.class);
                intent.putExtra("sID", shops.get(position).getId());
                intent.putExtra("sName", shops.get(position).getName());
                intent.putExtra("sSearchLink", shops.get(position).getSearchLink());
                intent.putExtra("sImageLink", shops.get(position).getImage());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.no_animation); // remember to put it after startActivity, if you put it to above, animation will not working
// document say if we don't want animation we can put 0. However, if we put 0 instead of R.anim.no_animation, the exist activity will become black when animate
            }
        });
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getShops().observe(this, shops -> {
            this.shops = shops;
            shopItemAdapter.setShopAdapter(MainActivity.this, shops);
            shopItemAdapter.notifyDataSetChanged();

        });

    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}