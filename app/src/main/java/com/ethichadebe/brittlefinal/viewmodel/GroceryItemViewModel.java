package com.ethichadebe.brittlefinal.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.repository.PriceCheckRepo;

import java.util.ArrayList;
import java.util.List;

public class GroceryItemViewModel extends AndroidViewModel {
    private static final String TAG = "GroceryItemViewModel";
    private final PriceCheckRepo repository;

    public GroceryItemViewModel(@NonNull Application application) {
        super(application);
        repository = new PriceCheckRepo(application);
    }

    /*public void insert(String name, double price, String image, boolean checked, int shopId){
        repository.setGroceryItems();
        repository.insertGroceryItem(name,price,image,checked,shopId);
    }*/

    public void insert(GroceryItem item){
        repository.setGroceryItems();
        repository.insertGroceryItem(item);
    }
    public void update(GroceryItem groceryItem){
        Log.d(TAG, "update: Updated");
        repository.updateGroceryItem(groceryItem);
    }

    public void delete(GroceryItem groceryItem){
        repository.deleteGroceryItem(groceryItem);
    }
    public void deleteAllItems(int sID){
        repository.deleteAllGroceryItems(sID);
    }

    public LiveData<List<GroceryItem>> getGroceryItems(int sID){
        repository.setGroceryItems();
        return repository.getItems(sID);
    }

    public LiveData<List<GroceryItem>> getAllGroceryItems(){
        repository.setGroceryItems();
        return repository.getAllItems();
    }
    public int countShopItems(int sID){
        return repository.countShopItems(sID);
    }
    public int countAllItems(){
        repository.setGroceryItems();
        return repository.countAllItems();
    }
}
