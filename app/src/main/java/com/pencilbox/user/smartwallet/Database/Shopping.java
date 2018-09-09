package com.pencilbox.user.smartwallet.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tbl_shopping")
public class Shopping {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shopping_id")
    private int shoppingId;
    @ColumnInfo(name = "shopping_title")
    private String title;
    @ColumnInfo(name = "shopping_status")
    private int shoppingStatus;

    @Ignore
    public Shopping(int shoppingId, String title, int shoppingStatus) {
        this.shoppingId = shoppingId;
        this.title = title;
        this.shoppingStatus = shoppingStatus;
    }

    public Shopping(String title, int shoppingStatus) {
        this.title = title;
        this.shoppingStatus = shoppingStatus;
    }

    public int getShoppingId() {
        return shoppingId;
    }

    public void setShoppingId(int shoppingId) {
        this.shoppingId = shoppingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getShoppingStatus() {
        return shoppingStatus;
    }

    public void setShoppingStatus(int shoppingStatus) {
        this.shoppingStatus = shoppingStatus;
    }
}
