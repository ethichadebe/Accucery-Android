package com.ethichadebe.brittlefinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethichadebe.brittlefinal.adapters.ShopItemAdapter;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.viewmodel.GroceryItemViewModel;
import com.ethichadebe.brittlefinal.viewmodel.ShopViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private List<Shop> shops = new ArrayList<>();


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
                Intent intent = new Intent(MainActivity.this, GroceryListActivity.class);
                intent.putExtra("sID", shops.get(position).getId());
                intent.putExtra("sName", shops.get(position).getName());
                intent.putExtra("sSearchLink", shops.get(position).getSearchLink());
                intent.putExtra("sImageLink", shops.get(position).getImage());
                intent.putExtra("sImageLink", shops.get(position).getImage());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.no_animation); // remember to put it after startActivity, if you put it to above, animation will not working
// document say if we don't want animation we can put 0. However, if we put 0 instead of R.anim.no_animation, the exist activity will become black when animate
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
                        }else {
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