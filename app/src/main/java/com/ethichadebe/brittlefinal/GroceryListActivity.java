package com.ethichadebe.brittlefinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.viewmodel.GroceryItemViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GroceryListActivity extends AppCompatActivity {
    private static final String TAG = "GroceryListActivity";
    private ExtendedFloatingActionButton fabAddShop;
    private TextView tvPrice, tvShopName;
    private RecyclerView rvItems;
    private RelativeLayout rlBack;
    private GroceryItemViewModel groceryItemViewModel;
    private List<GroceryItem> groceryItems = new ArrayList<>();
    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        tvPrice = findViewById(R.id.tvPrice);
        tvShopName = findViewById(R.id.tvShopName);
        fabAddShop = findViewById(R.id.fabAddShop);
        rlBack = findViewById(R.id.rlBack);

        fabAddShop.setOnClickListener(view -> {
            startActivity(new Intent(GroceryListActivity.this, CircularShopActivity.class));
        });

        groceryItemViewModel = new ViewModelProvider(GroceryListActivity.this).get(GroceryItemViewModel.class);
        rlBack.setOnClickListener(view -> startActivity(new Intent(GroceryListActivity.this, MainActivity.class)));

    }

    private void setupGroceryList(int position) {
        startCountAnim(0, totalPrice(), 3000, tvPrice);
        rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(GroceryListActivity.this));
        rvItems.setHasFixedSize(true);
        final GroceryItemAdapter groceryItemAdapter = new GroceryItemAdapter();
        rvItems.setAdapter(groceryItemAdapter);
        groceryItemViewModel.setGroceryItems(position);
        groceryItemViewModel.getGroceryItems().observe(GroceryListActivity.this, groceryItems -> {
            this.groceryItems = groceryItems;
            Log.d(TAG, "onCreate: grocery Items" + groceryItems.size());
            groceryItemAdapter.setGroceryItemAdapter(GroceryListActivity.this, groceryItems);
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
    }

    private int totalPrice() {
        int total = 0;

        for (GroceryItem item : groceryItems) {
            total += (item.getPrice() * item.getQuantity());
        }

        return total;
    }
    private void startCountAnim(int from, int to, int duration, TextView textView) {
        animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(valueAnimator -> textView.setText(animator.getAnimatedValue().toString()));
        animator.start();
    }
    public void back(View view) {
        startActivity(new Intent(GroceryListActivity.this, MainActivity.class));
    }

}