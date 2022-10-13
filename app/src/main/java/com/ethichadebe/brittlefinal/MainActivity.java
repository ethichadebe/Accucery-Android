package com.ethichadebe.brittlefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ethichadebe.brittlefinal.adapters.GroceryItemAdapter;
import com.ethichadebe.brittlefinal.adapters.ShopItemAdapter;
import com.ethichadebe.brittlefinal.local.dao.GroceryItemDao;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.viewmodel.GroceryItemViewModel;
import com.ethichadebe.brittlefinal.viewmodel.ShopViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ShopViewModel shopViewModel;
    private GroceryItemViewModel groceryItemViewModel;

    private RecyclerView rvShops, rvItems;

    private BottomSheetBehavior mBehavior;
    private View bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomSheet = findViewById(R.id.rlBottomSheet);

        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setPeekHeight(0, true);

        rvShops = findViewById(R.id.rvShops);
        rvShops.setLayoutManager(new GridLayoutManager(this,2));
        rvShops.setHasFixedSize(true);
        final ShopItemAdapter shopItemAdapter = new ShopItemAdapter();
        rvShops.setAdapter(shopItemAdapter);

        shopItemAdapter.setOnItemClickListener(new ShopItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                rvItems = findViewById(R.id.rvItems);
                rvItems.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rvItems.setHasFixedSize(true);
                final GroceryItemAdapter groceryItemAdapter = new GroceryItemAdapter();
                rvItems.setAdapter(groceryItemAdapter);
                groceryItemViewModel = new ViewModelProvider(MainActivity.this).get(GroceryItemViewModel.class);
                groceryItemViewModel.getGroceryItems(position).observe(MainActivity.this, groceryItems -> {
                    Log.d(TAG, "onCreate: grocery Items" + groceryItems.size());
                    groceryItemAdapter.setGroceryItemAdapter(MainActivity.this, groceryItems);
                    groceryItemAdapter.notifyDataSetChanged();
                });
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onClearListClick(int position) {

            }
        });
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getShops().observe(this, shops -> {
            if (shops.size() > 0) {
                Log.d(TAG, "onCreate: ibiziwe");
                shopItemAdapter.setShopAdapter(MainActivity.this, shops);
                shopItemAdapter.notifyDataSetChanged();
            }
        });

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    public void back(View view) {
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onBackPressed() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            finish();
        }
    }
}