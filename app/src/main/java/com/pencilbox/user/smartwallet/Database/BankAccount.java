package com.pencilbox.user.smartwallet.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "bank_account")
public class BankAccount {
    @PrimaryKey(autoGenerate = true)
    private int accountId;
    @ColumnInfo(name = "bank_name")
    private String bankName;
    @ColumnInfo(name = "account_name")
    private String accountName;
    @ColumnInfo(name = "account_no")
    private String accountNo;
    @ColumnInfo(name = "total_balance")
    private double balance;

    public BankAccount(String bankName, String accountName, String accountNo, double balance) {
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNo = accountNo;
        this.balance = balance;
    }
    @Ignore
    public BankAccount(int accountId, String bankName, String accountName, String accountNo, double balance) {
        this.accountId = accountId;
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNo = accountNo;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
