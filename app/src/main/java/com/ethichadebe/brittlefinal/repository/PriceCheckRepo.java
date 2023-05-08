package com.ethichadebe.brittlefinal.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ethichadebe.brittlefinal.local.PriceCheckDB;
import com.ethichadebe.brittlefinal.local.dao.GroceryItemDao;
import com.ethichadebe.brittlefinal.local.dao.ShopDao;
import com.ethichadebe.brittlefinal.local.dao.UserDao;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.local.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PriceCheckRepo {
    private GroceryItemDao groceryItemDao;
    private UserDao userDao;
    private final ShopDao shopDao;
    private final LiveData<List<Shop>> shops;

    private final PriceCheckDB db;

    public PriceCheckRepo(Application application) {
        db = PriceCheckDB.getInstance(application);
        shopDao = db.shopDao();
        userDao = db.userDao();
        shops = shopDao.getShops();
    }

    public void setGroceryItems() {
        groceryItemDao = db.groceryItemDao();
    }

    /*public void insertGroceryItem(String name, double price, String image,boolean checked, int shopId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> groceryItemDao.insert(name,price,image,checked,shopId));

    }*/
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

    public void updateAllShop(List<Shop> shops) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> shopDao.update(shops));
    }

    public void updateUser(User user) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> userDao.update(user));
    }

    public LiveData<List<Shop>> getShops() {
        return shops;
    }
    public LiveData<User> getUser() {
        return userDao.getUser();
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
