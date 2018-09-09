package com.pencilbox.user.smartwallet.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tbl_shopping_details")
public class ShoppingDetails {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shopping_details_id")
    private int detailsId;
    @ColumnInfo(name = "shopping_id")
    private int shoppingId;
    @ColumnInfo(name = "item_name")
    private String itemName;
    @ColumnInfo(name = "item_qty")
    private int quantity;
    @ColumnInfo(name = "item_price")
    private double price;
    @ColumnInfo(name = "item_status")
    private int itemStatus;

    public ShoppingDetails(int shoppingId, String itemName, int quantity, double price, int itemStatus) {
        this.shoppingId = shoppingId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.itemStatus = itemStatus;
    }

    @Ignore
    public ShoppingDetails(int detailsId, int shoppingId, String itemName, int quantity, double price, int itemStatus) {
        this.detailsId = detailsId;
        this.shoppingId = shoppingId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.itemStatus = itemStatus;
    }

    public int getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(int detailsId) {
        this.detailsId = detailsId;
    }

    public int getShoppingId() {
        return shoppingId;
    }

    public void setShoppingId(int shoppingId) {
        this.shoppingId = shoppingId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(int itemStatus) {
        this.itemStatus = itemStatus;
    }
}
