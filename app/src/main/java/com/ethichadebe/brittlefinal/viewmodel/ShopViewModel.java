package com.ethichadebe.brittlefinal.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.repository.PriceCheckRepo;

import java.util.ArrayList;
import java.util.List;

public class ShopViewModel extends AndroidViewModel {
    private final PriceCheckRepo repository;
    private final LiveData<List<Shop>> shops;


    public ShopViewModel(@NonNull Application application) {
        super(application);
        repository = new PriceCheckRepo(application);
        shops = repository.getShops();
    }

    public void update(Shop shop){
        repository.updateShop(shop);
    }

    public LiveData<List<Shop>> getShops(){
        return shops;
    }

    public LiveData<Shop> getShop(int sID) {
        return repository.getShop(sID);
    }
}
