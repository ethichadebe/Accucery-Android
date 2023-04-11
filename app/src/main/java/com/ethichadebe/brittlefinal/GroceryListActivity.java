package com.ethichadebe.brittlefinal;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethichadebe.brittlefinal.adapters.GroceryItemAdapter;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.viewmodel.GroceryItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class GroceryListActivity extends AppCompatActivity {
    private static final String TAG = "GroceryListActivity";
    private FloatingActionButton fabAddShop;
    private TextView tvPrice, tvShopName;
    private RecyclerView rvItems;
    private RelativeLayout rlClearList;
    private GroceryItemAdapter groceryItemAdapter;

    private GroceryItemViewModel groceryItemViewModel;
    private List<GroceryItem> groceryItems = new ArrayList<>();
    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        groceryItemAdapter = new GroceryItemAdapter();
        tvPrice = findViewById(R.id.tvPrice);
        tvShopName = findViewById(R.id.tvShopName);
        fabAddShop = findViewById(R.id.fabAddShop);
        rlClearList = findViewById(R.id.rlClearList);


        tvShopName.setText(getIntent().getStringExtra("sName"));
        fabAddShop.setOnClickListener(view -> {
            Intent intent = new Intent(GroceryListActivity.this, CircularShopActivity.class);
            intent.putExtra("sID", getIntent().getIntExtra("sID", 0));
            intent.putExtra("sSearchLink", getIntent().getStringExtra("sSearchLink"));
            intent.putExtra("sImageLink", getIntent().getStringExtra("sImageLink"));
            intent.putExtra("sName", getIntent().getStringExtra("sName"));
            startActivity(intent);
        });

        rlClearList.setOnClickListener(view -> {
            if (groceryItems.size() > 0) {
                int sID = groceryItems.get(0).getShopId();
                groceryItems.clear();
                groceryItemViewModel.deleteAllItems(sID);
                groceryItemAdapter.notifyDataSetChanged();
            }
        });

        groceryItemViewModel = new ViewModelProvider(GroceryListActivity.this).get(GroceryItemViewModel.class);
        setupGroceryList(getIntent().getIntExtra("sID", 0));
    }

    private void setupGroceryList(int position) {
        startCountAnim(0, totalPrice(), 3000, tvPrice);
        rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(GroceryListActivity.this));
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(groceryItemAdapter);
        groceryItemViewModel.getGroceryItems(position).observe(GroceryListActivity.this, groceryItems -> {
            this.groceryItems.add(new GroceryItem("",0.0,"",0));
            this.groceryItems.addAll(groceryItems);
            Log.d(TAG, "onCreate: grocery Items" + groceryItems.size());
            groceryItemAdapter.setGroceryItemAdapter(GroceryListActivity.this, groceryItems);
            groceryItemAdapter.notifyDataSetChanged();
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvItems);

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

            @Override
            public void onItemClick(int position) {
                GroceryItem item = groceryItems.get(position);
                GroceryItem newItem = new GroceryItem(item.getName(), item.getPrice(), item.getImage(), item.getShopId(), !item.isChecked());
                groceryItemViewModel.update(newItem);

                if (item.isChecked()) {
                    groceryItems.remove(position);
                    groceryItemAdapter.notifyItemRemoved(position);
                    Log.d(TAG, "onItemClick: item name" + newItem.getName() +" "+ item.getName());
                    groceryItems.add(1, newItem);
                    groceryItemAdapter.notifyItemInserted(1);
                    rvItems.smoothScrollToPosition(0);
                } else {
                    groceryItems.remove(position);
                    groceryItemAdapter.notifyItemRemoved(position);
                    groceryItems.add(newItem);
                    groceryItemAdapter.notifyItemInserted(groceryItems.size() - 1);
                    rvItems.smoothScrollToPosition(groceryItems.size());
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GroceryListActivity.this, MainActivity.class));
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d(TAG, "onSwiped: swiping...");
            int position = viewHolder.getBindingAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                GroceryItem deletedItem = groceryItems.get(position);
                groceryItems.remove(position);
                groceryItemViewModel.delete(deletedItem);
                groceryItemAdapter.notifyItemRemoved(position);

                Snackbar.make(rvItems, deletedItem.getName() + " has been removed.",
                                BaseTransientBottomBar.LENGTH_LONG).setAction("Undo", view -> {
                            groceryItems.add(position, deletedItem);
                            groceryItemViewModel.insert(deletedItem);
                            groceryItemAdapter.notifyItemInserted(position);
                        })
                        .setBackgroundTint(getResources().getColor(R.color.Red))
                        .setTextColor(Color.WHITE)
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(GroceryListActivity.this, R.color.Red))
                    .addActionIcon(R.drawable.delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}