package com.ethichadebe.brittlefinal.local.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ShopsTable")
public class Shop {

    @PrimaryKey
    private int id;                 //Shop id
    private String name;            //Shop name
    private String image;           //Shop logo URL

    private String searchLink;           //Shop search link
    private int itemsCount = 0;           //Shop search link
    private double price = 0;           //Shop search link
    private boolean isActive;          //Shop is active or not
    private boolean isOpen;          //Shop Open for selection

    public Shop(int id, String name, String image, String searchLink, boolean isOpen, boolean isActive) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.searchLink = searchLink;
        this.isOpen = isOpen;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSearchLink() {
        return searchLink;
    }

    public void setSearchLink(String searchLink) {
        this.searchLink = searchLink;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
