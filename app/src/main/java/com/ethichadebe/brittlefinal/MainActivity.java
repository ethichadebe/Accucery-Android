package com.ethichadebe.brittlefinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethichadebe.brittlefinal.adapters.ShopItemAdapter;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.viewmodel.ShopViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.no_animation); // remember to put it after startActivity, if you put it to above, animation will not working
// document say if we don't want animation we can put 0. However, if we put 0 instead of R.anim.no_animation, the exist activity will become black when animate
            }
        });
        ShopViewModel shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getShops().observe(this, shops -> {
            this.shops = shops;
            shopItemAdapter.setShopAdapter(MainActivity.this, shops);
            shopItemAdapter.notifyDataSetChanged();

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

}