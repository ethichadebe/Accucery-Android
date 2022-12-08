package com.ethichadebe.brittlefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ethichadebe.brittlefinal.adapters.GroceryItemAdapter;
import com.ethichadebe.brittlefinal.adapters.ShopItemAdapter;
import com.ethichadebe.brittlefinal.local.dao.GroceryItemDao;
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

    private TextView tvPrice;
    private BottomSheetBehavior mBehavior;
    private View bottomSheet;

    private ExtendedFloatingActionButton fabAddShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomSheet = findViewById(R.id.rlBottomSheet);
        tvPrice = findViewById(R.id.tvPrice);
        fabAddShop = findViewById(R.id.fabAddShop);

        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setPeekHeight(0, true);

        rvShops = findViewById(R.id.rvShops);
        fabAddShop.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CircularShopActivity.class)));
        rvShops.setLayoutManager(new LinearLayoutManager(this));
        rvShops.setHasFixedSize(true);
        final ShopItemAdapter shopItemAdapter = new ShopItemAdapter();
        rvShops.setAdapter(shopItemAdapter);

        shopItemAdapter.setOnItemClickListener(new ShopItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startCountAnim(0, totalPrice(), 3000, tvPrice);
                rvItems = findViewById(R.id.rvItems);
                rvItems.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rvItems.setHasFixedSize(true);
                final GroceryItemAdapter groceryItemAdapter = new GroceryItemAdapter();
                rvItems.setAdapter(groceryItemAdapter);
                groceryItemViewModel = new ViewModelProvider(MainActivity.this).get(GroceryItemViewModel.class);
                groceryItemViewModel.getGroceryItems(position).observe(MainActivity.this, groceryItems -> {
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