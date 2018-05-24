package com.pencilbox.user.smartwallet.ViewModel;

import android.app.AlertDialog;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.pencilbox.user.smartwallet.Database.BankAccount;
import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.R;
import com.pencilbox.user.smartwallet.Utils.ExpenseDialog;

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

    public void withdraw(BankAccount account, Context context){
        ExpenseDialog.createDialogForDepositOrWithdraw(context, "Withdraw", new ExpenseDialog.OnUpdateBalanceListener() {
            @Override
            public void onUpdateBalance(double amount) {
                account.setBalance(account.getBalance() - amount);
                int updatedRow = expenseDatabase.expenseDao().updateBalance(account);
                if (updatedRow > 0){
                    Toast.makeText(context, "Withdrawn", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void deposit(BankAccount account, Context context){
        ExpenseDialog.createDialogForDepositOrWithdraw(context, "Deposit", new ExpenseDialog.OnUpdateBalanceListener() {
            @Override
            public void onUpdateBalance(double amount) {
                account.setBalance(account.getBalance() + amount);
                int updatedRow = expenseDatabase.expenseDao().updateBalance(account);
                if (updatedRow > 0){
                    Toast.makeText(context, "Deposited", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
