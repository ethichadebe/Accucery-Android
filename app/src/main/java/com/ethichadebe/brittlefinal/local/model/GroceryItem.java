package com.ethichadebe.brittlefinal.local.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "GroceryItemsTable")
public class GroceryItem implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int itemId;                 //Grocery item id generated locally
    private String name;            //Grocery item name
    private double price;           //Grocery item price
    private String image;           //Grocery item image URL
    private boolean checked = false;             //Grocery item shopID
    private boolean isActive = true;             //Grocery item shopID
    private int quantity;
    private int shopId;             //Grocery item shopID

    public GroceryItem(String name, double price, String image, int shopId) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.checked = false;
        this.isActive = true;
        this.quantity = 1;
        this.shopId = shopId;
    }

    protected GroceryItem(Parcel in) {
        itemId = in.readInt();
        name = in.readString();
        price = in.readDouble();
        image = in.readString();
        shopId = in.readInt();
    }

    public static final Creator<GroceryItem> CREATOR = new Creator<GroceryItem>() {
        @Override
        public GroceryItem createFromParcel(Parcel in) {
            return new GroceryItem(in);
        }

        @Override
        public GroceryItem[] newArray(int size) {
            return new GroceryItem[size];
        }
    };

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

    public boolean isChecked() {
        return checked;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setChecked(boolean checked) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(itemId);
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeString(image);
        parcel.writeInt(shopId);
    }
}
