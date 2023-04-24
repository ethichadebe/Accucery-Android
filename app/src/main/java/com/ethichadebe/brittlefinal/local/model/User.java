package com.ethichadebe.brittlefinal.local.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserTable")
public class User {

    @PrimaryKey
    private int userId;                 //User id generated locally
    private String name;            //Grocery item name
    private int coins;

    public User(int userId, String name, int coins) {
        this.userId = userId;
        this.name = name;
        this.coins = coins;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
