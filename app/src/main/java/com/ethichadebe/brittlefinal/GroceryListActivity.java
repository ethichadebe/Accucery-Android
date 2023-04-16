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
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
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
    private final List<GroceryItem> groceryItems = new ArrayList<>();
    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        MobileAds.initialize(this);

        groceryItemAdapter = new GroceryItemAdapter();
        tvUnchecked = findViewById(R.id.tvUnchecked);
        tvChecked = findViewById(R.id.tvChecked);
        tvTotal = findViewById(R.id.tvTotal);
        TextView tvShopName = findViewById(R.id.tvShopName);
        FloatingActionButton fabAddShop = findViewById(R.id.fabAddShop);
        RelativeLayout rlClearList = findViewById(R.id.rlClearList);


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
                groceryItems.clear();
                int sID = groceryItems.get(0).getShopId();
                groceryItemViewModel.deleteAllItems(sID);
                groceryItemAdapter.notifyDataSetChanged();
            }
        });

        groceryItemViewModel = new ViewModelProvider(GroceryListActivity.this).get(GroceryItemViewModel.class);
        setupGroceryList(getIntent().getIntExtra("sID", 0));
    }

    private void setupGroceryList(int position) {
        startCountAnim(0, totalPrice(UNCHECKED), 3000, tvUnchecked);
        startCountAnim(0, totalPrice(CHECKED), 3000, tvChecked);
        startCountAnim(0, totalPrice(WHOLE_LIST), 3000, tvTotal);
        rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(GroceryListActivity.this));
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(groceryItemAdapter);
        groceryItemViewModel.getGroceryItems(position).observe(GroceryListActivity.this, groceryItems -> {
            this.groceryItems.add(new GroceryItem("", 0.0, "", 0));
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
                int checked = totalPrice(UNCHECKED);
                int unchecked = totalPrice(UNCHECKED);
                int total = totalPrice(WHOLE_LIST);
                GroceryItem item = groceryItems.get(position);
                item.setQuantity(item.getQuantity() + 1);
                groceryItemViewModel.update(item);
                groceryItems.get(position).setQuantity(item.getQuantity());
                groceryItemAdapter.notifyItemChanged(position);
                startCountAnim(unchecked, totalPrice(UNCHECKED), 3000, tvUnchecked);
                startCountAnim(checked, totalPrice(CHECKED), 3000, tvChecked);
                startCountAnim(total, totalPrice(WHOLE_LIST), 3000, tvTotal);
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
                    startCountAnim(unchecked, totalPrice(UNCHECKED), 3000, tvUnchecked);
                    startCountAnim(checked, totalPrice(CHECKED), 3000, tvChecked);
                    startCountAnim(total, totalPrice(WHOLE_LIST), 3000, tvTotal);
                }
            }

            @Override
            public void onItemClick(int position) {
                int checked = totalPrice(UNCHECKED);
                int unchecked = totalPrice(UNCHECKED);
                int total = totalPrice(WHOLE_LIST);
                GroceryItem item = groceryItems.get(position);

                if (item.isChecked()) {
                    groceryItems.remove(position);
                    groceryItemAdapter.notifyItemRemoved(position);
                    for (GroceryItem item1 : groceryItems) {
                        Log.d(TAG, "onItemClick: item name" + item1.getName() + "\n");
                    }
                    groceryItems.add(2, new GroceryItem(item.getName(), item.getPrice(), item.getImage(), item.getShopId(), !item.isChecked()));
                    groceryItemAdapter.notifyItemInserted(2);
                } else {
                    groceryItems.remove(position);
                    groceryItemAdapter.notifyItemRemoved(position);
                    groceryItems.add(new GroceryItem(item.getName(), item.getPrice(), item.getImage(), item.getShopId(), !item.isChecked()));
                    groceryItemAdapter.notifyItemInserted(groceryItems.size() - 1);
                }

                groceryItemViewModel.update(new GroceryItem(item.getName(), item.getPrice(), item.getImage(), item.getShopId(), !item.isChecked()));

                startCountAnim(unchecked, totalPrice(UNCHECKED), 3000, tvUnchecked);
                startCountAnim(checked, totalPrice(CHECKED), 3000, tvChecked);
                startCountAnim(total, totalPrice(WHOLE_LIST), 3000, tvTotal);
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

        Log.d(TAG, "totalPrice: UNCHECKED " + uncheckedTotal);
        Log.d(TAG, "totalPrice: CHECKED " + checkedTotal);
        Log.d(TAG, "totalPrice: TOTAL " + wholeTotal);
        return total.get(type);
    }

    private void startCountAnim(int from, int to, int duration, TextView textView) {
        DecimalFormat decim = new DecimalFormat("#,###");
        animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(valueAnimator -> textView.setText(decim.format(animator.getAnimatedValue())));
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
                        .setBackgroundTint(ContextCompat.getColor(getApplicationContext(), R.color.Red))
                        .setTextColor(Color.WHITE)
                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white))
                        .show();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(GroceryListActivity.this, R.color.Red))
                    .addActionIcon(R.drawable.delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}