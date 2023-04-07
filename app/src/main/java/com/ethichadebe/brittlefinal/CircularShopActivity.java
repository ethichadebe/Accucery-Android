package com.ethichadebe.brittlefinal;

import android.animation.Animator;
import android.os.AsyncTask;
import android.os.Build;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethichadebe.brittlefinal.adapters.GroceryItemSearchAdapter;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CircularShopActivity extends AppCompatActivity {
    private static final String TAG = "CircularShopActivity";

    private FrameLayout root_layout;

    private List<Shop> shops = new ArrayList<>();
    private GroceryItemSearchAdapter groceryItemAdapter;
    private RecyclerView rvGroceryItemSearch;

    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_circular_shop);

        root_layout = findViewById(R.id.root_layout);
        etSearch = findViewById(R.id.etSearch);
        rvGroceryItemSearch = findViewById(R.id.rvGroceryItemSearch);

        setupAnimation(savedInstanceState);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            private Timer timer = new Timer();
            private final long DELAY = 1000; // Milliseconds

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>3){
                    setupShops();

                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    Scraper scraper = new Scraper();
                                    scraper.execute("https://www.shoprite.co.za/search/all?q="+ charSequence);
                                    Log.d(TAG, "onTextChanged: " +charSequence);
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
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            root_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            root_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
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

        groceryItemAdapter.setOnItemClickListener(new GroceryItemSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }


    public void back(View view) {
        finish();
    }


    class Scraper extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            groceryItemAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(String... url) {
            try {
                Log.d(TAG, "doInBackground: trying....: " + url[0]);
                ArrayList<GroceryItem> items = new ArrayList<>();
                Document document = Jsoup.connect(url[0]).get();
                Elements data = document.select("figure.item-product__content");

                Log.d(TAG, "doInBackground: size " +data);
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    String image = data.select("figure.item-product__content").select("img").eq(i).attr("src");
                    String name = data.select("h3.item-product__name").select("a").eq(i).text();
                    String price = data.select("div.special-price__price").select("span").eq(i).text();

                    Log.d(TAG, "doInBackground: Item: " + i+1 +"------------------------------------------------------------------");
                    Log.d(TAG, "doInBackground: name: " + name);
                    Log.d(TAG, "doInBackground: price: " + price);
                    Log.d(TAG, "doInBackground: image: " + image);
                    items.add(new GroceryItem(name, Double.parseDouble(price.replace("R","")), image, 0, 1));
                    groceryItemAdapter.setGroceryItemSearchAdapter(CircularShopActivity.this, items);
                }
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: error "+e.getMessage());
                throw new RuntimeException(e);
            }

            return null;
        }
    }

}