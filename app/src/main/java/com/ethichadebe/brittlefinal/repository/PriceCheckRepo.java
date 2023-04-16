package com.ethichadebe.brittlefinal.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

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

import java.util.List;

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
        new InsertGroceryItemAsyncTask(groceryItemDao).execute(groceryItem);
    }

    public void updateGroceryItem(GroceryItem groceryItem) {
        new UpdateGroceryItemAsyncTask(groceryItemDao).execute(groceryItem);
    }

    public void deleteGroceryItem(GroceryItem groceryItem) {
        new DeleteGroceryItemAsyncTask(groceryItemDao).execute(groceryItem);
    }

    public void deleteAllGroceryItems(int sId) {
        new DeleteAllGroceryItemAsyncTask(groceryItemDao, sId).execute();
    }

    public void insertShop(Shop shop) {
        new InsertShopAsyncTask(shopDao).execute(shop);
    }

    public void updateShop(Shop shop) {
        new UpdateShopAsyncTask(shopDao).execute(shop);
    }

    public void deleteShop(Shop shop) {
        new DeleteShopAsyncTask(shopDao).execute(shop);
    }

    public void deleteAllShops() {
        new DeleteAllShopsAsyncTask(shopDao).execute();
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
    public int countAllItems() {
        return groceryItemDao.countAllItems();
    }
    public int countShopItems(int sID) {
        return groceryItemDao.countShopItems(sID);
    }

}
