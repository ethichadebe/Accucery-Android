package com.ethichadebe.brittlefinal.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ethichadebe.brittlefinal.local.model.Shop;

import java.util.List;

@Dao
public interface ShopDao {

    /**
     * Insert Grocery Item to local database
     * @param shop Grocery Item to be inserted
     */
    @Insert
    void insert(Shop shop);

    /**
     * Delete Grocery Item to local database
     * @param shop Grocery Item to be deleted
     */
    @Update
    void update(Shop shop);

    @Delete
    void delete(Shop shop);

    @Query("DELETE FROM ShopsTable")
    void deleteAllShops();

    @Query("SELECT * FROM ShopsTable")
    LiveData<List<Shop>> getShops();

    @Query("SELECT * FROM ShopsTable WHERE isActive = :isActive")
    LiveData<List<Shop>> getActiveShop(boolean isActive);
}
