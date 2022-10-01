package com.ethichadebe.brittlefinal.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ethichadebe.brittlefinal.local.PriceCheckDB;
import com.ethichadebe.brittlefinal.local.ShopDao;
import com.ethichadebe.brittlefinal.local.model.GroceryItem;
import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.local.scyncTasks.DeleteAllShopsAsyncTask;
import com.ethichadebe.brittlefinal.local.scyncTasks.DeleteShopAsyncTask;
import com.ethichadebe.brittlefinal.local.scyncTasks.InsertShopAsyncTask;
import com.ethichadebe.brittlefinal.local.scyncTasks.UpdateShopAsyncTask;

import java.util.List;

public class PriceCheckRepo {
    //private GroceryItemDao groceryItemDao;
    private LiveData<List<GroceryItem>> groceryItems;

    private ShopDao shopDao;
    private LiveData<List<Shop>> shops;

    public PriceCheckRepo(Application application) {
        PriceCheckDB db = PriceCheckDB.getInstance(application);
        /*groceryItemDao = db.groceryItemDao();
        groceryItems = groceryItemDao.getShopItems();*/

        shopDao = db.shopDao();
        shops = shopDao.getShops();
    }

    /*public void insertGroceryItem(GroceryItem groceryItem) {
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
    }*/

    public LiveData<List<GroceryItem>> getShopGroceryItems() {
        return groceryItems;
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

}
