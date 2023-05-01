package com.ethichadebe.brittlefinal.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ethichadebe.brittlefinal.local.model.GroceryItem;

import java.util.List;

@Dao
public interface GroceryItemDao {

    /**
     * Insert Grocery Item to local database
     *
     * @param gi Grocery Item to be inserted
     */
    @Insert
    void insert(GroceryItem gi);

    @Query("INSERT OR REPLACE INTO GroceryItemsTable (name, price, image, shopId) values (:name, :price, :image, :shopId);")
    void insert(String name, double price, String image, int shopId);


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
