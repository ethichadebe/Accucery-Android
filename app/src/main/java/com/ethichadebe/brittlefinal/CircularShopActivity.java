package com.ethichadebe.brittlefinal;

import android.animation.Animator;
import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethichadebe.brittlefinal.adapters.GroceryItemSearchAdapter;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.viewmodel.GroceryItemViewModel;

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

    private GroceryItemViewModel groceryItemViewModel;
    private FrameLayout root_layout;
    private GroceryItemSearchAdapter groceryItemAdapter;
    private RecyclerView rvGroceryItemSearch;

    private ArrayList<GroceryItem> items;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_circular_shop);

        items = new ArrayList<>();
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
            private final long DELAY = 2000; // Milliseconds

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 3) {
                    setupShops();

                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    Scraper scraper = new Scraper();
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

        groceryItemAdapter.setOnItemClickListener(position -> {
            GroceryItem item = items.get(position);
            groceryItemViewModel.insert(new GroceryItem(item.getName(), item.getPrice(), item.getImage(), getIntent().getIntExtra("sID", 0)));
            etSearch.setText("");
            items = null;

            Intent intent = new Intent(CircularShopActivity.this, GroceryListActivity.class);

            intent.putExtra("sID",getIntent().getIntExtra("sID",0));
            intent.putExtra("sSearchLink",getIntent().getStringExtra("sSearchLink"));
            intent.putExtra("sImageLink",getIntent().getStringExtra("sImageLink"));
            intent.putExtra("sName",getIntent().getStringExtra("sName"));

            startActivity(intent);
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
                items = new ArrayList<>();
                if (url[0].contains("https://www.pnp.co.za/pnpstorefront/pnp/en/search/?text=")) {
                    Document document = Jsoup.connect(url[0]).get();
                    Elements data = document.select("div.productCarouselItem");

                    Log.d(TAG, "doInBackground: size " + data);
                    int size = data.size();
                    for (int i = 0; i < size; i++) {
                        String image = data.select("div.thumb").select("img").eq(i).attr("src");
                        String name = data.select("div.item-name").eq(i).text();
                        String price = data.select("div.product-price").select("div.currentPrice").eq(i).text();

                        if (price.isEmpty()){
                            price="0.0";
                        }
                        Log.d(TAG, "doInBackground: Item: " + i + "------------------------------------------------------------------");
                        Log.d(TAG, "doInBackground: name: " + name);
                        Log.d(TAG, "doInBackground: price: " + price);
                        Log.d(TAG, "doInBackground: image: " + image);
                        items.add(new GroceryItem(name, Double.parseDouble(price.replaceAll("[^\\d.]", "")),
                                image, getIntent().getIntExtra("sID", 0)));
                        groceryItemAdapter.setGroceryItemSearchAdapter(CircularShopActivity.this, items);
                    }
                } else if (url[0].contains("https://www.woolworths.co.za/cat?Ntt=")) {
                    Log.d(TAG, "doInBackground: it does contain");
                    Document document = Jsoup.connect(url[0] + "&Dy=1").get();
                    Elements data = document.select("div.product-list__item");

                    Log.d(TAG, "doInBackground: size " + document);
                    int size = data.size();
                    for (int i = 0; i < size; i++) {
                        String image = data.select("div.product--image").select("img").eq(i).attr("src");
                        String name = data.select("div.product-card__name").select("a").eq(i).text();
                        String price = data.select("div.product__price").select("strong.price").eq(i).text();

                        Log.d(TAG, "doInBackground: Item: " + i + "------------------------------------------------------------------");
                        Log.d(TAG, "doInBackground: name: " + name);
                        Log.d(TAG, "doInBackground: price: " + price);
                        Log.d(TAG, "doInBackground: image: " + image);
                        items.add(new GroceryItem(name, Double.parseDouble(price.replaceAll("[^\\d.]", "")),
                                image, getIntent().getIntExtra("sID", 0)));
                        groceryItemAdapter.setGroceryItemSearchAdapter(CircularShopActivity.this, items);
                    }

                } else if (url[0].contains("https://www.makro.co.za/search/?text=")) {
                    Log.d(TAG, "doInBackground: it does contain");
                    Document document = Jsoup.connect(url[0] + "&Dy=1").get();
                    Elements data = document.select("div.mak-product-tiles-container__product-tile");

                    Log.d(TAG, "doInBackground: size " + document);
                    int size = data.size();
                    for (int i = 0; i < size; i++) {
                        String image = data.select("a.product-tile-inner__img").select("img").eq(i).attr("src");
                        String name = data.select("a.product-tile-inner__productTitle").select("span").eq(i).text();
                        String price = data.select("p.price").select("span.mak-save-price").eq(i).text() + "." +
                                data.select("p.price").select("span.mak-product__cents").eq(i).text();

                        Log.d(TAG, "doInBackground: Item: " + i + "------------------------------------------------------------------");
                        Log.d(TAG, "doInBackground: name: " + name);
                        Log.d(TAG, "doInBackground: price: " + price);
                        Log.d(TAG, "doInBackground: image: " + image);
                        items.add(new GroceryItem(name, Double.parseDouble(price.replaceAll("[^\\d.]", "")),
                                image, getIntent().getIntExtra("sID", 0)));
                        groceryItemAdapter.setGroceryItemSearchAdapter(CircularShopActivity.this, items);
                    }

                } else if (url[0].contains("https://www.game.co.za/l/search/?t=")) {
                    Log.d(TAG, "doInBackground: it does contain");
                    Document document = Jsoup.connect(url[0] + "&q=" + url[0].replace("https://www.game.co.za/l/search/?t=", "")
                            + "%3Arelevance").get();
                    Elements data = document.select("div.r-14lw9ot");

                    Log.d(TAG, "doInBackground: size " + document);
                    int size = data.size();
                    for (int i = 0; i < size; i++) {
                        String image = data.select("div.r-1p0dtai").select("img").eq(i).attr("src");
                        String name = data.select("a.css-4rbku5").select("div").eq(i).text();
                        String price = data.select("div.css-901oao").eq(i).text().replace(",", ".");

                        Log.d(TAG, "doInBackground: Item: " + i + "------------------------------------------------------------------");
                        Log.d(TAG, "doInBackground: name: " + name);
                        Log.d(TAG, "doInBackground: price: " + price);
                        Log.d(TAG, "doInBackground: image: " + image);
                        items.add(new GroceryItem(name, Double.parseDouble(price.replaceAll("[^\\d.]", "")),
                                image, getIntent().getIntExtra("sID", 0)));
                        groceryItemAdapter.setGroceryItemSearchAdapter(CircularShopActivity.this, items);
                    }

                } else {
                    Log.d(TAG, "doInBackground: it does contain");
                    Document document = Jsoup.connect(url[0]).get();
                    Elements data = document.select("figure.item-product__content");

                    Log.d(TAG, "doInBackground: size " + data);
                    int size = data.size();
                    for (int i = 0; i < size; i++) {
                        String image = data.select("figure.item-product__content").select("img").eq(i).attr("src");
                        String name = data.select("h3.item-product__name").select("a").eq(i).text();
                        String price = data.select("div.special-price__price").select("span").eq(i).text();

                        Log.d(TAG, "doInBackground: Item: " + i + 1 + "------------------------------------------------------------------");
                        Log.d(TAG, "doInBackground: name: " + name);
                        Log.d(TAG, "doInBackground: price: " + price);
                        Log.d(TAG, "doInBackground: image: " + image);
                        Log.d(TAG, "doInBackground: full image " + getIntent().getStringExtra("sImageLink") + image);
                        items.add(new GroceryItem(name, Double.parseDouble(price.replaceAll("[^\\d.]", "")),
                                "https://www.shoprite.co.za/" + image, 1));
                        groceryItemAdapter.setGroceryItemSearchAdapter(CircularShopActivity.this, items);
                    }

                }
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: error " + e.getMessage());
                throw new RuntimeException(e);
            }

            return null;
        }
    }

}