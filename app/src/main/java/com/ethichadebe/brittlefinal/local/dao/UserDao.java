package com.ethichadebe.brittlefinal.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ethichadebe.brittlefinal.local.model.Shop;
import com.ethichadebe.brittlefinal.local.model.User;

import java.util.List;

@Dao
public interface UserDao {

    /**
     * Insert Grocery Item to local database
     * @param user Grocery Item to be inserted
     */
    @Insert
    void insert(User user);

    /**
     * Delete Grocery Item to local database
     * @param user Grocery Item to be deleted
     */
    @Update
    void update(User user);

    @Query("SELECT * FROM UserTable")
    LiveData<User> getUser();

}
