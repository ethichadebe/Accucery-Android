package com.ethichadebe.brittlefinal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethichadebe.brittlefinal.adapters.GroceryItemSearchAdapter;
import com.ethichadebe.brittlefinal.adapters.ShopItemAdapter;
import com.ethichadebe.brittlefinal.layout_manager.WrapContentGridLayoutManager;
import com.ethichadebe.brittlefinal.layout_manager.WrapContentLinearLayoutManager;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.local.model.User;
import com.ethichadebe.brittlefinal.viewmodel.GroceryItemViewModel;
import com.ethichadebe.brittlefinal.viewmodel.ShopViewModel;
import com.ethichadebe.brittlefinal.viewmodel.UserViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    public static final int BACK = 10;
    public static final int COMPARE = 20;

    private static final String TAG = "MainActivity";
    private RecyclerView rvItems;
    private BottomSheetBehavior bottomSheetBehavior;
    private ShopViewModel shopViewModel;
    private GroceryItemSearchAdapter groceryItemAdapter;
    private List<GroceryItem> newItems;
    private TextView tvShopName;
    private User user;
    private GroceryItemViewModel groceryItemViewModel;
    private UserViewModel userViewModel;

    private RewardedAd rewardedAd;
    private List<Shop> shops = new ArrayList<>();
    Dialog dialog;

    private void popupAd() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.coin_ad);

        TextView tvWatchAd = dialog.findViewById(R.id.tvWatchAd);
        TextView tvBuyTokes = dialog.findViewById(R.id.tvBuyTokes);
        TextView tvDismiss = dialog.findViewById(R.id.tvDismiss);

        tvDismiss.setOnClickListener(view -> dialog.dismiss());
        tvWatchAd.setOnClickListener(view -> {
            if (rewardedAd != null) {
                Activity activityContext = MainActivity.this;
                rewardedAd.show(activityContext, rewardItem -> {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    userViewModel.update(new User(0, "Mtho", 5));
                    for (int i = 0; i < shops.size(); i++) {
                        shops.get(i).setOpen(true);
                        shopViewModel.update(shops.get(i));
                    }
                    dialog.dismiss();
                    Log.d(TAG, "popupAd: Reward amount -> " + rewardAmount + " Reward type -> " + rewardType);
                });
            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.popupAnimation;
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        groceryItemViewModel = new ViewModelProvider(this).get(GroceryItemViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        RecyclerView rvShops = findViewById(R.id.rvShops);
        NestedScrollView nsvBottomSheet = findViewById(R.id.nsvBottomSheet);
        TextView tvClose = findViewById(R.id.tvClose);
        rvItems = findViewById(R.id.rvItems);
        tvShopName = findViewById(R.id.tvShopName);
        rvShops.setLayoutManager(new WrapContentGridLayoutManager(this, 2));
        rvShops.setHasFixedSize(true);
        final ShopItemAdapter shopItemAdapter = new ShopItemAdapter();
        rvShops.setAdapter(shopItemAdapter);

        bottomSheetBehavior = BottomSheetBehavior.from(nsvBottomSheet);

        tvClose.setOnClickListener(view -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));

        shopItemAdapter.setOnItemClickListener(position -> {
            if (shops.get(position).isActive()) {
                Log.d(TAG, "onCreate: coins " + user.getCoins());
                if (shops.get(position).isOpen()) {
                    Intent intent = new Intent(MainActivity.this, GroceryListActivity.class);
                    intent.putExtra("sID", shops.get(position).getId());
                    intent.putExtra("sName", shops.get(position).getName());
                    intent.putExtra("sSearchLink", shops.get(position).getSearchLink());
                    intent.putExtra("sImageLink", shops.get(position).getImage());
                    intent.putExtra("sImageLink", shops.get(position).getImage());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                    // remember to put it after startActivity, if you put it to above, animation will not working
                    // document say if we don't want animation we can put 0. However, if we put 0 instead of R.anim.no_animation, the exist activity will become black when animate
                } else {
                    popupAd();
                }
            }
        });

        shopViewModel.getShops().observe(this, shops -> {
            this.shops = shops;

            userViewModel.getUser().observe(this, user -> {
                this.user = user;
                groceryItemViewModel.getAllGroceryItems().observe(this, groceryItems -> {
                    if (groceryItems.size() > 0) {
                        Shop shop = null;
                        for (int i = 0; i < shops.size(); i++) {
                            if (shops.get(i).getId() == groceryItems.get(0).getShopId()) {
                                this.shops.get(i).setOpen(true);
                                shop = shops.get(i);
                            }
                        }

                        Shop finalShop = shop;
                        Log.d(TAG, "onCreate: coins " + user.getCoins());
                        if (getIntent().getIntExtra("back", 0) == BACK && user.getCoins() == 0) {
                            Intent intent = new Intent(MainActivity.this, GroceryListActivity.class);
                            assert finalShop != null;
                            intent.putExtra("sID", finalShop.getId());
                            intent.putExtra("sName", finalShop.getName());
                            intent.putExtra("sSearchLink", finalShop.getSearchLink());
                            intent.putExtra("sImageLink", finalShop.getImage());
                            intent.putExtra("sImageLink", finalShop.getImage());
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_up, R.anim.no_animation); // remember to put it after startActivity, if you put it to above, animation will not working
                        } else if ((getIntent().getIntExtra("back", 0) == COMPARE)) {
                            tvShopName.setText(getIntent().getStringExtra("sName"));
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            newItems = new ArrayList<>();
                            Timer timer = new Timer();
                            setupItems();

                            scheduleItemSearch(timer, groceryItems);
                        }
                    }
                });
                shopItemAdapter.setShopAdapter(MainActivity.this, shops);
                shopItemAdapter.notifyItemRangeInserted(0, shops.size() - 1);
            });

        });

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });


    }

    private void scheduleItemSearch(Timer timer, List<GroceryItem> groceryItems) {
        timer.cancel();
        timer = new Timer();
        // Milliseconds
        long DELAY = 2000;
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        new InsertShopAsyncTask(groceryItems).execute();
                    }
                },
                DELAY
        );

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void scrapeData(String url, int itemID) {
        try {
            Log.d(TAG, "doInBackground: trying....: " + url);
            if (url.contains("https://www.pnp.co.za/pnpstorefront/pnp/en/search/?text=")) {
                Document document = Jsoup.connect(url).get();
                Elements data = document.select("div.productCarouselItem");

                //Log.d(TAG, "doInBackground: size " + data);
                for (int i = 0; i < data.size(); i++) {
                    String image = data.select("div.thumb").select("img").eq(i).attr("src");
                    String name = data.select("div.item-name").eq(i).text();
                    String price = data.select("div.product-price").select("div.currentPrice").eq(i).text().replaceAll("[^\\d.]", "");

                    cleanItem(name, price, itemID, image);
                }

            } else if (url.contains("https://www.woolworths.co.za/cat?Ntt=")) {
                Log.d(TAG, "doInBackground: it does contain");
                Document document = Jsoup.connect(url + "&Dy=1").get();
                Elements data = document.select("div.product-list__item");

                Log.d(TAG, "doInBackground: size " + document);
                String image = data.select("div.product--image").select("img").eq(0).attr("src");
                String name = data.select("div.product-card__name").select("a").eq(0).text();
                String price = data.select("div.product__price").select("strong.price").eq(0).text().replaceAll("[^\\d.]", "");

                cleanItem(name, price, itemID, image);

            } else if (url.contains("https://www.makro.co.za/search/?text=")) {
                Log.d(TAG, "doInBackground: it does contain");
                Document document = Jsoup.connect(url + "&Dy=1").get();
                Elements data = document.select("div.mak-product-tiles-container__product-tile");

                Log.d(TAG, "doInBackground: size " + document);
                String image = data.select("a.product-tile-inner__img").select("img").eq(0).attr("src");
                String name = data.select("a.product-tile-inner__productTitle").select("span").eq(0).text();
                String price = data.select("p.price").select("span.mak-save-price").eq(0).text() + "." +
                        data.select("p.price").select("span.mak-product__cents").eq(0).text().replaceAll("[^\\d.]", "");

                Log.d(TAG, "scrapeData: price " + price);
                cleanItem(name, price, itemID, image);

            } else if (url.contains("https://www.game.co.za/l/search/?t=")) {
                Log.d(TAG, "doInBackground: it does contain");
                Document document = Jsoup.connect(url + "&q=" + url.replace("https://www.game.co.za/l/search/?t=", "")
                        + "%3Arelevance").get();
                Elements data = document.select("div.r-14lw9ot");

                Log.d(TAG, "doInBackground: size " + document);
                String image = data.select("div.r-1p0dtai").select("img").eq(0).attr("src");
                String name = data.select("a.css-4rbku5").select("div").eq(0).text();
                String price = data.select("div.css-901oao").eq(0).text().replace(",", ".")
                        .replaceAll("[^\\d.]", "");

                cleanItem(name, price, itemID, image);

            } else {
                Log.d(TAG, "doInBackground: it does contain");
                Document document = Jsoup.connect(url).get();
                Elements data = document.select("figure.item-product__content");

                //Log.d(TAG, "doInBackground: size " + data);
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    String image = data.select("figure.item-product__content").select("img").eq(i).attr("src");
                    String name = data.select("h3.item-product__name").select("a").eq(i).text();
                    String price = data.select("div.special-price__price").select("span").eq(i).text().replaceAll("[^\\d.]", "");

                    cleanItem(name, price, itemID, "https://www.shoprite.co.za/" + image);
                }
                groceryItemAdapter.notifyItemInserted(newItems.size() - 1);


            }
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: error " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void setupItems() {
        rvItems.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvItems.setHasFixedSize(true);
        groceryItemAdapter = new GroceryItemSearchAdapter();
        rvItems.setAdapter(groceryItemAdapter);

        groceryItemAdapter.setOnItemClickListener(position -> {
            Log.d(TAG, "setupShops: position " + position + " size " + newItems.size());
            GroceryItem item = newItems.get(position);
            groceryItemViewModel.insert(new GroceryItem(item.getName(), item.getPrice(), item.getImage(), item.getQuantity(), getIntent().getIntExtra("sID", 0)));

            for (int i = 0; i < newItems.size(); i++) {
                Log.d(TAG, "setupShops: true");
                if (newItems.get(i).getQuantity() == item.getQuantity()) {
                    newItems.remove(newItems.get(i));
                    groceryItemAdapter.notifyItemRangeRemoved(0, i);
                }
            }

        });

        groceryItemAdapter.setGroceryItemSearchAdapter(this, newItems);

    }


    private void cleanItem(String name, String price, int itemID, String image) {
        if (price.isEmpty()) {
            price = "0.0";
        }
        Log.d(TAG, "doInBackground: Item ID: " + itemID);
        Log.d(TAG, "doInBackground: name: " + name);
        Log.d(TAG, "doInBackground: price: " + price.replaceAll("[^\\d.]", ""));
        Log.d(TAG, "doInBackground: image: " + image);
        newItems.add(new GroceryItem(name, Double.parseDouble(price.replace("R ", "").replaceAll("[^\\d.]", "")),
                image, itemID, getIntent().getIntExtra("sID", 0)));
    }

    public class InsertShopAsyncTask extends AsyncTask<Shop, Void, Void> {
        private final List<GroceryItem> groceryItems;

        public InsertShopAsyncTask(List<GroceryItem> groceryItems) {
            this.groceryItems = groceryItems;
        }

        @Override
        protected Void doInBackground(Shop... shops) {
            if (groceryItems.size() > 0) {
                scrapeData("https://www.shoprite.co.za/search/all?q=" + groceryItems.get(0).getName(), groceryItems.get(0).getItemId());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (groceryItems.size() > 0) {
                groceryItems.remove(0);
                new InsertShopAsyncTask(groceryItems).execute();
            }
        }
    }
}