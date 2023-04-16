package com.ethichadebe.brittlefinal;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethichadebe.brittlefinal.Util.Scraper;
import com.ethichadebe.brittlefinal.adapters.GroceryItemSearchAdapter;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.viewmodel.GroceryItemViewModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CircularShopActivity extends AppCompatActivity {
    private static final String TAG = "CircularShopActivity";

    private GroceryItemViewModel groceryItemViewModel;
    private FrameLayout root_layout;
    private GroceryItemSearchAdapter groceryItemAdapter;
    private RecyclerView rvGroceryItemSearch;

    private EditText etSearch;
    private Scraper scraper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_circular_shop);

        root_layout = findViewById(R.id.root_layout);
        etSearch = findViewById(R.id.etSearch);
        rvGroceryItemSearch = findViewById(R.id.rvGroceryItemSearch);
        groceryItemViewModel = new ViewModelProvider(CircularShopActivity.this).get(GroceryItemViewModel.class);

        setupAnimation(savedInstanceState);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            private Timer timer = new Timer();

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 3) {
                    setupShops();

                    timer.cancel();
                    timer = new Timer();
                    // Milliseconds
                    long DELAY = 2000;
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    scraper = new Scraper(getIntent(), groceryItemAdapter, getApplicationContext());
                                    scraper.execute(getIntent().getStringExtra("sSearchLink") + charSequence);
                                    Log.d(TAG, "onTextChanged: " + charSequence);
                                    // TODO: Do what you need here (refresh list).
                                    // You will probably need to use
                                    // runOnUiThread(Runnable action) for some
                                    // specific actions (e.g., manipulating views).
                                }
                            },
                            DELAY
                    );
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        etSearch.addTextChangedListener(watcher);
    }

    private void setupAnimation(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            root_layout.setVisibility(View.INVISIBLE);
            root_layout.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = root_layout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        root_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }

    }

    private void circularRevealActivity() {

        int cx = root_layout.getWidth();
        int cy = root_layout.getHeight();

        float finalRadius = Math.max(root_layout.getWidth(), root_layout.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(root_layout, cx, cy, 0, finalRadius);
        circularReveal.setDuration(500);

        // make the view visible and start the animation
        root_layout.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    private void setupShops() {
        rvGroceryItemSearch.setLayoutManager(new LinearLayoutManager(this));
        rvGroceryItemSearch.setHasFixedSize(true);
        groceryItemAdapter = new GroceryItemSearchAdapter();
        rvGroceryItemSearch.setAdapter(groceryItemAdapter);

        groceryItemAdapter.setOnItemClickListener(position -> {
            Log.d(TAG, "setupShops: position " +position + " size " + scraper.getItems().size());
            GroceryItem item = scraper.getItems().get(position);
            groceryItemViewModel.insert(new GroceryItem(item.getName(), item.getPrice(), item.getImage(), getIntent().getIntExtra("sID", 0)));
            etSearch.setText("");

            Intent intent = new Intent(CircularShopActivity.this, GroceryListActivity.class);

            intent.putExtra("sID", getIntent().getIntExtra("sID", 0));
            intent.putExtra("sSearchLink", getIntent().getStringExtra("sSearchLink"));
            intent.putExtra("sImageLink", getIntent().getStringExtra("sImageLink"));
            intent.putExtra("sName", getIntent().getStringExtra("sName"));

            startActivity(intent);
            finish();
        });
    }


    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}