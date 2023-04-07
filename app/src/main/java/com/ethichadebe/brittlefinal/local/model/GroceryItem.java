package com.ethichadebe.brittlefinal.local.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "GroceryItemsTable")
public class GroceryItem {

    @PrimaryKey(autoGenerate = true)
    private int itemId;                 //Grocery item id generated locally
    private String name;            //Grocery item name
    private double price;           //Grocery item price
    private String image;           //Grocery item image URL
    private int checked = 0;             //Grocery item shopID
    private int quantity;
    private int shopId;             //Grocery item shopID

    public GroceryItem(String name, double price, String image, int checked, int shopId) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.checked = checked;
        this.quantity = 1;
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

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
