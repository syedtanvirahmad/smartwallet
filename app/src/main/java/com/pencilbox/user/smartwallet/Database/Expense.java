package com.pencilbox.user.smartwallet.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by User on 11/21/2017.
 */
@Entity(tableName = "expense")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "expense_name")
    private String expenseName;
    @ColumnInfo(name = "expense_amount")
    private double expenseAmount;
    @ColumnInfo(name = "expense_date_time")
    private String expenseDateTime;

    public Expense(String expenseName, double expenseAmount, String expenseDateTime) {
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
        this.expenseDateTime = expenseDateTime;
    }

    @Ignore
    public Expense() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseDateTime() {
        return expenseDateTime;
    }

    public void setExpenseDateTime(String expenseDateTime) {
        this.expenseDateTime = expenseDateTime;
    }
}
