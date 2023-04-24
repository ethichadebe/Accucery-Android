package com.ethichadebe.brittlefinal;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ethichadebe.brittlefinal.adapters.GroceryItemAdapter;
import com.ethichadebe.brittlefinal.adapters.ShopItemAdapter;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.viewmodel.GroceryItemViewModel;
import com.ethichadebe.brittlefinal.viewmodel.ShopViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class GroceryListActivity extends AppCompatActivity {
    public static final int UNCHECKED = 0;
    public static final int CHECKED = 1;
    public static final int WHOLE_LIST = 2;
    private static final String TAG = "GroceryListActivity";
    private TextView tvUnchecked;
    private TextView tvChecked;
    private TextView tvTotal;
    private RecyclerView rvItems;
    private GroceryItemAdapter groceryItemAdapter;

    private GroceryItemViewModel groceryItemViewModel;
    private ShopViewModel shopViewModel;
    private List<GroceryItem> groceryItems = new ArrayList<>();
    private List<Shop> shops = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        MobileAds.initialize(this);

        groceryItemAdapter = new GroceryItemAdapter();
        tvUnchecked = findViewById(R.id.tvUnchecked);
        tvChecked = findViewById(R.id.tvChecked);
        tvTotal = findViewById(R.id.tvTotal);
        ImageView ivShopLogo = findViewById(R.id.ivShopLogo);
        TextView tvShopName = findViewById(R.id.tvShopName);
        FloatingActionButton fabAddShop = findViewById(R.id.fabAddShop);
        RelativeLayout rlClearList = findViewById(R.id.rlClearList);
        Glide.with(this).load(getIntent().getStringExtra("sImageLink")).placeholder(R.mipmap.ic_launcher).into(ivShopLogo);

      /*  TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(R.id.fabAddShop), "This is a target", "We have the best targets, believe me")
                        // All options below are optional
                        .outerCircleColor(R.color.primary_green)        // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)                        // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.met_baseColor)       // Specify a color for the target circle
                        .titleTextSize(20)                          // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)                  // Specify the color of the title text
                        .descriptionTextSize(10)                    // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.Red)              // Specify the color of the description text
                        .textColor(R.color.black)                       // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)              // Specify a typeface for the text
                        .dimColor(R.color.black)                        // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                               // Whether to draw a drop shadow or not
                        .cancelable(true)                         // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                               // Whether to tint the target view's color
                        .transparentTarget(true)                        // Specify whether the target is transparent (displays the content underneath)
                        .targetRadius(60),                              // Specify the target radius (in dp)
                new TapTargetView.Listener() {                          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);                      // This call is optional
                        Intent intent = new Intent(GroceryListActivity.this, CircularShopActivity.class);
                        intent.putExtra("sID", getIntent().getIntExtra("sID", 0));
                        intent.putExtra("sSearchLink", getIntent().getStringExtra("sSearchLink"));
                        intent.putExtra("sImageLink", getIntent().getStringExtra("sImageLink"));
                        intent.putExtra("sName", getIntent().getStringExtra("sName"));
                        startActivity(intent);
                    }
                });*/


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
            int size = groceryItems.size();
            if (size > 0) {
                groceryItems.clear();
                groceryItemAdapter.notifyItemRangeRemoved(0, groceryItems.size() - 1);
                groceryItemViewModel.deleteAllItems(getIntent().getIntExtra("sID", 0));
                openCloseShop(true);
            }
        });

        groceryItemViewModel = new ViewModelProvider(this).get(GroceryItemViewModel.class);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getShops().observe(this, shops1 -> {
            shops = shops1;
            Log.d(TAG, "setupGroceryList: total " + shops.size());
        });
        setupGroceryList(getIntent().getIntExtra("sID", 0));
    }

    private void setupGroceryList(int sID) {
        rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(groceryItemAdapter);
        groceryItemViewModel.getGroceryItems(sID).observe(this, groceryItems -> {
            this.groceryItems = groceryItems;
            startCountAnim(0, totalPrice(UNCHECKED), tvUnchecked);
            startCountAnim(0, totalPrice(CHECKED), tvChecked);
            startCountAnim(0, totalPrice(WHOLE_LIST), tvTotal);

            Log.d(TAG, "onCreate: grocery Items" + groceryItems.size());
            groceryItemAdapter.setGroceryItemAdapter(this, groceryItems);
            groceryItemAdapter.notifyDataSetChanged();
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvItems);

        groceryItemAdapter.setOnItemClickListener(new GroceryItemAdapter.OnItemClickListener() {
            @Override
            public void onAddQuantityClick(int position) {
                int checked = totalPrice(UNCHECKED);
                int unchecked = totalPrice(UNCHECKED);
                int total = totalPrice(WHOLE_LIST);
                GroceryItem item = groceryItems.get(position);
                item.setQuantity(item.getQuantity() + 1);
                groceryItemViewModel.update(item);
                groceryItems.get(position).setQuantity(item.getQuantity());
                groceryItemAdapter.notifyItemChanged(position);
                startCountAnim(unchecked, totalPrice(UNCHECKED), tvUnchecked);
                startCountAnim(checked, totalPrice(CHECKED), tvChecked);
                startCountAnim(total, totalPrice(WHOLE_LIST), tvTotal);
            }

            @Override
            public void onSubQuantityClick(int position) {
                int checked = totalPrice(UNCHECKED);
                int unchecked = totalPrice(UNCHECKED);
                int total = totalPrice(WHOLE_LIST);
                GroceryItem item = groceryItems.get(position);
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    groceryItemViewModel.update(item);
                    groceryItems.get(position).setQuantity(item.getQuantity());
                    groceryItemAdapter.notifyItemChanged(position);
                    startCountAnim(unchecked, totalPrice(UNCHECKED), tvUnchecked);
                    startCountAnim(checked, totalPrice(CHECKED), tvChecked);
                    startCountAnim(total, totalPrice(WHOLE_LIST), tvTotal);
                }
            }

            @Override
            public void onItemClick(int position) {
                int checked = totalPrice(UNCHECKED);
                int unchecked = totalPrice(UNCHECKED);
                int total = totalPrice(WHOLE_LIST);
                GroceryItem item = groceryItems.get(position);

                Log.d(TAG, "onItemClick: Item ID = " + item.getItemId());
                if (item.isChecked()) {
                    groceryItems.remove(position);
                    groceryItemAdapter.notifyItemRemoved(position);

                    item.setChecked(!item.isChecked());
                    groceryItems.add(0, item);
                    groceryItemAdapter.notifyItemInserted(0);
                } else {
                    item.setChecked(!item.isChecked());
                    groceryItems.remove(position);
                    groceryItemAdapter.notifyItemRemoved(position);
                    groceryItems.add(item);
                    groceryItemAdapter.notifyItemInserted(groceryItems.size() - 1);
                }

                groceryItemViewModel.update(item);

                Log.d(TAG, "----------------------------------------------------------------------------------------------------------");
                Log.d(TAG, "totalPrice: UNCHECKED " + totalPrice(UNCHECKED));
                Log.d(TAG, "totalPrice: CHECKED " + totalPrice(CHECKED));
                Log.d(TAG, "totalPrice: TOTAL " + totalPrice(WHOLE_LIST));
                startCountAnim(unchecked, totalPrice(UNCHECKED), tvUnchecked);
                startCountAnim(checked, totalPrice(CHECKED), tvChecked);
                startCountAnim(total, totalPrice(WHOLE_LIST), tvTotal);
            }
        });
    }

    private int totalPrice(int type) {
        ArrayList<Integer> total = new ArrayList<>();
        int uncheckedTotal = 0, checkedTotal = 0, wholeTotal = 0;

        for (GroceryItem item : groceryItems) {
            switch (type) {
                case UNCHECKED:
                    if (!item.isChecked()) {
                        uncheckedTotal += item.getPrice() * item.getQuantity();
                    }
                    break;
                case CHECKED:
                    if (item.isChecked()) {
                        checkedTotal += (item.getPrice() * item.getQuantity());
                    }
                    break;
                case WHOLE_LIST:
                    wholeTotal += (item.getPrice() * item.getQuantity());
                    break;
            }
        }

        total.add(uncheckedTotal);
        total.add(checkedTotal);
        total.add(wholeTotal);

        return total.get(type);
    }

    private void startCountAnim(int from, int to, TextView textView) {
        Log.d(TAG, "startCountAnim: " + to);
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(3000);
        animator.addUpdateListener(valueAnimator -> textView.setText(this.getResources().getString(R.string.r0_0, animator.getAnimatedValue())));
        animator.start();
    }

    public void back(View view) {
        for (Shop shop : shops) {
            if (shop.getId() == getIntent().getIntExtra("sID", 0)) {
                shop.setItemsCount(groceryItems.size());
                double totalPrice = 0;
                for (GroceryItem groceryItem : groceryItems) {
                    totalPrice += totalPrice + (groceryItem.getPrice() * groceryItem.getQuantity());
                }
                shop.setPrice(totalPrice);
                shopViewModel.update(shop);
            }
        }

        Intent intent = new Intent(GroceryListActivity.this, MainActivity.class);
        openCloseShop(false);
        Log.d(TAG, "onBindViewHolder: size " + groceryItems.size() + "----------------------------------------------------------------------------------");
        if (groceryItems.size() > 0) {
            openCloseShop(false);
        }
        intent.putExtra("back", true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        for (Shop shop : shops) {
            if (shop.getId() == getIntent().getIntExtra("sID", 0)) {
                shop.setItemsCount(groceryItems.size());
                double totalPrice = 0;
                for (GroceryItem groceryItem : groceryItems) {
                    totalPrice += totalPrice + (groceryItem.getPrice() * groceryItem.getQuantity());
                }
                shop.setPrice(totalPrice);
                shopViewModel.update(shop);
            }
        }
        Intent intent = new Intent(GroceryListActivity.this, MainActivity.class);
        Log.d(TAG, "onBindViewHolder: size " + groceryItems.size() + "----------------------------------------------------------------------------------");
        if (groceryItems.size() > 0) {
            openCloseShop(false);
        }
        intent.putExtra("back", true);
        startActivity(intent);
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

                if (groceryItems.size() == 0) {
                    openCloseShop(true);
                }

                Snackbar.make(rvItems, deletedItem.getName() + " has been removed.", BaseTransientBottomBar.LENGTH_LONG).setAction("Undo", view -> {
                    groceryItems.add(position, deletedItem);
                    groceryItemViewModel.insert(deletedItem);
                    groceryItemAdapter.notifyItemInserted(position);
                    openCloseShop(false);
                }).setBackgroundTint(ContextCompat.getColor(getApplicationContext(), R.color.Red)).setTextColor(Color.WHITE).setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white)).show();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive).addBackgroundColor(ContextCompat.getColor(GroceryListActivity.this, R.color.Red)).addActionIcon(R.drawable.delete_24).create().decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void openCloseShop(boolean emptyList) {
        for (int i = 0; i < shops.size(); i++) {
            if (emptyList) {
                shops.get(i).setOpen(true);
            } else {
                shops.get(i).setOpen(shops.get(i).getId() == getIntent().getIntExtra("sID", 0));
            }
            shopViewModel.update(shops.get(i));
        }
    }

}