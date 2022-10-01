package com.ethichadebe.brittlefinal.local.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "GroceryItemsTable")
public class GroceryItem {

    @PrimaryKey(autoGenerate = true)
    private int itemId;                 //Grocery item id generated locally
    private String name;            //Grocery item name
    private Double price;           //Grocery item price
    private String image;           //Grocery item image URL
    private int shopId;             //Grocery item shopID

    public GroceryItem(String name, Double price, String image, int shopId) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.shopId = shopId;
    }

        public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setsId(int shopId) {
        this.shopId = shopId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getShopId() {
        return shopId;
    }
}
