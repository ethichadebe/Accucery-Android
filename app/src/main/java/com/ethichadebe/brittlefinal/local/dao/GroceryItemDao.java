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
     * @param gi Grocery Item to be inserted
     */
    @Insert
    void insert(GroceryItem gi);

    /**
     * Delete Grocery Item to local database
     * @param gi Grocery Item to be deleted
     */
    @Update
    void update(GroceryItem gi);

    @Delete
    void delete(GroceryItem gi);

    @Query("SELECT * FROM GroceryItemsTable WHERE shopId = :shopId")
    LiveData<List<GroceryItem>> getShopItems(int shopId);

    @Query("DELETE FROM GroceryItemsTable WHERE shopId = :shopId")
    void deleteAllGroceryItems(int shopId);
}
