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

    private ValueAnimator animator;

    private List<Shop> shops = new ArrayList<>();
    private List<GroceryItem> groceryItems = new ArrayList<>();

    private ShopViewModel shopViewModel;
    private GroceryItemViewModel groceryItemViewModel;

    private RecyclerView rvShops, rvItems;

    private TextView tvPrice, tvShopName;
    private BottomSheetBehavior mBehavior;
    private View bottomSheet;

    private ExtendedFloatingActionButton fabAddShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomSheet = findViewById(R.id.rlBottomSheet);
        tvPrice = findViewById(R.id.tvPrice);
        tvShopName = findViewById(R.id.tvShopName);
        fabAddShop = findViewById(R.id.fabAddShop);
        fabAddShop.setVisibility(View.GONE);

        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setPeekHeight(0, true);

        rvShops = findViewById(R.id.rvShops);
        fabAddShop.setOnClickListener(view -> {

            startActivity(new Intent(MainActivity.this, CircularShopActivity.class));
        });
        rvShops.setLayoutManager(new GridLayoutManager(this, 2));
        rvShops.setHasFixedSize(true);
        final ShopItemAdapter shopItemAdapter = new ShopItemAdapter();
        rvShops.setAdapter(shopItemAdapter);

        shopItemAdapter.setOnItemClickListener(position -> {

            startCountAnim(0, totalPrice(), 3000, tvPrice);
            rvItems = findViewById(R.id.rvItems);
            rvItems.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            rvItems.setHasFixedSize(true);
            final GroceryItemAdapter groceryItemAdapter = new GroceryItemAdapter();
            rvItems.setAdapter(groceryItemAdapter);
            groceryItemViewModel = new ViewModelProvider(MainActivity.this).get(GroceryItemViewModel.class);
            groceryItemViewModel.getGroceryItems(position).observe(MainActivity.this, groceryItems -> {
                Log.d(TAG, "onCreate: shop name: " + shops.size());

                for (Shop shop : shops) {
                    if (shop.getId() == shops.get(position).getId()) {
                        shopViewModel.update(new Shop(shop.getId(), shop.getName(), shop.getImage(), shop.getSearchLink(), true));
                        Log.d(TAG, "onCreate: shop name: " + shop.getName());
                        tvShopName.setText(shop.getName());
                    } else {
                        shopViewModel.update(new Shop(shop.getId(), shop.getName(), shop.getImage(), shop.getSearchLink(), false));
                    }
                }
                setGroceryItems(groceryItems);
                Log.d(TAG, "onCreate: grocery Items" + groceryItems.size());
                groceryItemAdapter.setGroceryItemAdapter(MainActivity.this, groceryItems);
                groceryItemAdapter.notifyDataSetChanged();
            });


            groceryItemAdapter.setOnItemClickListener(new GroceryItemAdapter.OnItemClickListener() {
                @Override
                public void onAddQuantityClick(int position) {
                    int prevPrice = totalPrice();
                    GroceryItem item = groceryItems.get(position);
                    item.setQuantity(item.getQuantity() + 1);
                    groceryItemViewModel.update(item);
                    groceryItems.get(position).setQuantity(item.getQuantity());
                    groceryItemAdapter.notifyItemChanged(position);
                    startCountAnim(prevPrice, totalPrice(), 1000, tvPrice);
                }

                @Override
                public void onSubQuantityClick(int position) {
                    int prevPrice = totalPrice();
                    GroceryItem item = groceryItems.get(position);
                    if (item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                        groceryItemViewModel.update(item);
                        groceryItems.get(position).setQuantity(item.getQuantity());
                        groceryItemAdapter.notifyItemChanged(position);
                        startCountAnim(prevPrice, totalPrice(), 1000, tvPrice);
                    }
                }
            });

            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            fabAddShop.setVisibility(View.VISIBLE);
        });
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getShops().observe(this, shops -> {
            this.shops = shops;
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
        animator.end();
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            finish();
        }
    }

    private void setGroceryItems(List<GroceryItem> groceryItems) {
        this.groceryItems = groceryItems;
    }

    private void startCountAnim(int from, int to, int duration, TextView textView) {
        animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(valueAnimator -> textView.setText(animator.getAnimatedValue().toString()));
        animator.start();
    }

    private int totalPrice() {
        int total = 0;

        for (GroceryItem item : groceryItems) {
            total += (item.getPrice() * item.getQuantity());
        }

        return total;
    }
}