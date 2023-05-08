package com.ethichadebe.brittlefinal.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ethichadebe.brittlefinal.local.model.GroceryItem;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface GroceryItemDao {

    /*@Query("INSERT OR REPLACE INTO GroceryItemsTable (name, price, image, checked,active, shopId) values (:name, :price, :image, :checked,:active, :shopId);")
    void insert(String name, double price, String image, boolean checked, boolean active, int shopId);*/

    @Insert
    void insert(GroceryItem item);

    /**
     * Update Grocery Item to local database
     *
     * @param gi Grocery Item to be updated
     */
    @Update
    void update(GroceryItem gi);

    /**
     * Delete Grocery Item to local database
     *
     * @param gi Grocery Item to be deleted
     */
    @Delete
    void delete(GroceryItem gi);

    @Query("SELECT * FROM GroceryItemsTable WHERE shopId = :shopId ORDER BY checked")
    LiveData<List<GroceryItem>> getShopItems(int shopId);

    @Query("SELECT * FROM GroceryItemsTable")
    LiveData<List<GroceryItem>> getAllItems();

    @Query("SELECT COUNT(*) FROM GroceryItemsTable WHERE shopId = :shopId")
    int countShopItems(int shopId);

    @Query("SELECT COUNT(*) FROM GroceryItemsTable")
    int countAllItems();

    @Query("DELETE FROM GroceryItemsTable WHERE shopId = :shopId")
    void deleteAllGroceryItems(int shopId);
}
