package com.ethichadebe.brittlefinal.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ethichadebe.brittlefinal.local.dao.GroceryItemDao;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.repository.PriceCheckRepo;

import java.util.List;

public class GroceryItemViewModel extends AndroidViewModel {
    private static final String TAG = "GroceryItemViewModel";
    private PriceCheckRepo repository;
    private LiveData<List<GroceryItem>> groceryItems;


    public GroceryItemViewModel(@NonNull Application application) {
        super(application);
        repository = new PriceCheckRepo(application);
    }

    public void insert(GroceryItem groceryItem){
        repository.insertGroceryItem(groceryItem);
    }

    public void update(GroceryItem groceryItem){
        Log.d(TAG, "update: Updated");
        repository.updateGroceryItem(groceryItem);
    }

    public void delete(GroceryItem groceryItem){
        repository.deleteGroceryItem(groceryItem);
    }
    public void deleteAllItems(){
        repository.deleteAllShops();
    }

    public void setGroceryItems(int sID){
        repository.setGroceryItems(sID);
    }
    public LiveData<List<GroceryItem>> getGroceryItems(){
        return repository.getItems();
    }
    public int countShopItems(int sID){
        return repository.countShopItems(sID);
    }
    public int countAllItems(){
        return repository.countAllItems();
    }
}
