package com.pencilbox.user.smartwallet.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pencilbox.user.smartwallet.Database.BankAccount;
import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private Context context;
    private ExpenseDatabase expenseDatabase;
    private LiveData<List<BankAccount>>bankAccounts;
    public AccountViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        expenseDatabase = ExpenseDatabase.getInstance(application);
    }

    public boolean addBankAccount(BankAccount bankAccount){
        long insertedRow = expenseDatabase.expenseDao().addBankAccount(bankAccount);
        if (insertedRow > 0) {
            return true;
        }
        return false;
    }

    public LiveData<List<BankAccount>> getBankAccounts(){
        bankAccounts = expenseDatabase.expenseDao().getAllBankAccounts();
        return bankAccounts;
    }

}
