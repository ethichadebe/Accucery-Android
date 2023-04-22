package com.ethichadebe.brittlefinal.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.lifecycle.LiveData;

import com.ethichadebe.brittlefinal.R;
import com.ethichadebe.brittlefinal.local.PriceCheckDB;
import com.ethichadebe.brittlefinal.local.dao.GroceryItemDao;
import com.ethichadebe.brittlefinal.local.dao.ShopDao;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.local.scyncTasks.DeleteAllGroceryItemAsyncTask;
import com.ethichadebe.brittlefinal.local.scyncTasks.DeleteAllShopsAsyncTask;
import com.ethichadebe.brittlefinal.local.scyncTasks.DeleteGroceryItemAsyncTask;
import com.ethichadebe.brittlefinal.local.scyncTasks.DeleteShopAsyncTask;
import com.ethichadebe.brittlefinal.local.scyncTasks.InsertGroceryItemAsyncTask;
import com.ethichadebe.brittlefinal.local.scyncTasks.InsertShopAsyncTask;
import com.ethichadebe.brittlefinal.local.scyncTasks.UpdateGroceryItemAsyncTask;
import com.ethichadebe.brittlefinal.local.scyncTasks.UpdateShopAsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PriceCheckRepo {
    private GroceryItemDao groceryItemDao;

    private final ShopDao shopDao;
    private final LiveData<List<Shop>> shops;

    private final PriceCheckDB db;

    public PriceCheckRepo(Application application) {
        db = PriceCheckDB.getInstance(application);
        shopDao = db.shopDao();
        shops = shopDao.getShops();
    }

    public void setGroceryItems() {
        groceryItemDao = db.groceryItemDao();
    }

    public void insertGroceryItem(GroceryItem groceryItem) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> groceryItemDao.insert(groceryItem));

    }

    public void updateGroceryItem(GroceryItem groceryItem) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> groceryItemDao.update(groceryItem));
    }

    public void deleteGroceryItem(GroceryItem groceryItem) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> groceryItemDao.delete(groceryItem));
    }

    public void deleteAllGroceryItems(int sId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> groceryItemDao.deleteAllGroceryItems(sId));
    }

    public void insertShop(Shop shop) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> shopDao.insert(shop));
    }

    public void updateShop(Shop shop) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> shopDao.update(shop));
    }

    public void updateShop(ArrayList<Shop> shops) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> shopDao.update(shops));
    }

    public void deleteShop(Shop shop) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> shopDao.delete(shop));
    }

    public void deleteAllShops() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(shopDao::deleteAllShops);
    }

    public LiveData<List<Shop>> getShops() {
        return shops;
    }

    public LiveData<Shop> getShop(int sID) {
        return shopDao.getShop(sID);
    }

    public LiveData<List<GroceryItem>> getItems(int sID) {
        return groceryItemDao.getShopItems(sID);
    }

    public LiveData<List<GroceryItem>> getAllItems() {
        return groceryItemDao.getAllItems();
    }

    public int countAllItems() {
        return groceryItemDao.countAllItems();
    }

    public int countShopItems(int sID) {
        return groceryItemDao.countShopItems(sID);
    }

}
