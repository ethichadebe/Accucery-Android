package com.ethichadebe.brittlefinal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethichadebe.brittlefinal.adapters.ShopItemAdapter;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.viewmodel.GroceryItemViewModel;
import com.ethichadebe.brittlefinal.viewmodel.ShopViewModel;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

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
                });
            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
            }
        });
        //dialog.getWindow().setBackgroundDrawable();
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

        RecyclerView rvShops = findViewById(R.id.rvShops);
        rvShops.setLayoutManager(new GridLayoutManager(this, 2));
        rvShops.setHasFixedSize(true);
        final ShopItemAdapter shopItemAdapter = new ShopItemAdapter();
        rvShops.setAdapter(shopItemAdapter);

        shopItemAdapter.setOnItemClickListener(position -> {
            if (shops.get(position).isActive()) {
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
        ShopViewModel shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        GroceryItemViewModel groceryItemViewModel = new ViewModelProvider(this).get(GroceryItemViewModel.class);

        shopViewModel.getShops().observe(this, shops -> {
            this.shops = shops;

            groceryItemViewModel.getAllGroceryItems().observe(this, groceryItems -> {
                if (groceryItems.size() > 0) {
                    Shop shop = null;
                    for (int i = 0; i < shops.size(); i++) {
                        Log.d(TAG, "onCreate: is open? " + shops.get(i).isOpen());
                        if (shops.get(i).getId() == groceryItems.get(0).getShopId()) {
                            this.shops.get(i).setOpen(true);
                            shop = shops.get(i);
                        } else {
                            this.shops.get(i).setOpen(false);
                        }
                    }

                    if (!getIntent().getBooleanExtra("back", false)) {
                        Intent intent = new Intent(MainActivity.this, GroceryListActivity.class);
                        assert shop != null;
                        intent.putExtra("sID", shop.getId());
                        intent.putExtra("sName", shop.getName());
                        intent.putExtra("sSearchLink", shop.getSearchLink());
                        intent.putExtra("sImageLink", shop.getImage());
                        intent.putExtra("sImageLink", shop.getImage());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up, R.anim.no_animation); // remember to put it after startActivity, if you put it to above, animation will not working
                    }
                }
            });

            shopItemAdapter.setShopAdapter(MainActivity.this, shops);
            shopItemAdapter.notifyItemRangeInserted(0, shops.size() - 1);

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


    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}