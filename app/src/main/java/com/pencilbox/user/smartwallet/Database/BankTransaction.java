package com.pencilbox.user.smartwallet.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "transaction_info")
public class BankTransaction {
    @PrimaryKey(autoGenerate = true)
    private int transactionId;
    @ColumnInfo(name = "transaction_date")
    private String date;
    @ColumnInfo(name = "transaction_action")
    private String action;
    @ColumnInfo(name = "account_id")
    private int accountId;
    @ColumnInfo(name = "transaction_amount")
    private double transactionAmount;
    @ColumnInfo(name = "current_balance")
    private double currentBalance;

    public BankTransaction(String date, String action, int accountId, double transactionAmount, double currentBalance) {
        this.date = date;
        this.action = action;
        this.accountId = accountId;
        this.transactionAmount = transactionAmount;
        this.currentBalance = currentBalance;
    }

    @Ignore
    public BankTransaction(int transactionId, String date, String action, int accountId, double transactionAmount, double currentBalance) {
        this.transactionId = transactionId;
        this.date = date;
        this.action = action;
        this.accountId = accountId;
        this.transactionAmount = transactionAmount;
        this.currentBalance = currentBalance;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
}
