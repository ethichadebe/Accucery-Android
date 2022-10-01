package com.ethichadebe.brittlefinal.local.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ShopsTable")
public class Shop {

    @PrimaryKey(autoGenerate = true)
    private int id;                 //Shop id
    private String name;            //Shop name
    private String image;           //Shop logo URL
    private boolean isActive;          //Shop is active or not

    public Shop(int id, String name, String image, boolean isActive) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.isActive = isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setsId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getsId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
