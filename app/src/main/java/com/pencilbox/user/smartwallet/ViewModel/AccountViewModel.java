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
import com.pencilbox.user.smartwallet.Database.BankTransaction;
import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Database.TransactionDetailsPojo;
import com.pencilbox.user.smartwallet.R;
import com.pencilbox.user.smartwallet.Utils.Constants;
import com.pencilbox.user.smartwallet.Utils.ExpenseDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private Context context;
    private ExpenseDatabase expenseDatabase;
    private LiveData<List<BankAccount>>bankAccounts;
    private List<TransactionDetailsPojo>transactions;
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
        ExpenseDialog.createDialogForDepositOrWithdraw(context, Constants.Accounts.ACTION_WITHDRAW, amount -> {
            account.setBalance(account.getBalance() - amount);
            int updatedRow = expenseDatabase.expenseDao().updateBalance(account);
            if (updatedRow > 0){
                Toast.makeText(context, R.string.action_withdraw_done, Toast.LENGTH_SHORT).show();
            }
            BankTransaction transaction = new BankTransaction(getDate(),Constants.Accounts.ACTION_WITHDRAW,account.getAccountId(),amount,account.getBalance());
            long insertedRow = expenseDatabase.expenseDao().addTransaction(transaction);
        });
    }
    public void deposit(BankAccount account, Context context){
        ExpenseDialog.createDialogForDepositOrWithdraw(context, Constants.Accounts.ACTION_DEPOSIT, amount -> {
            account.setBalance(account.getBalance() + amount);
            int updatedRow = expenseDatabase.expenseDao().updateBalance(account);
            if (updatedRow > 0){
                Toast.makeText(context, R.string.action_deposit_done, Toast.LENGTH_SHORT).show();
            }
            BankTransaction transaction = new BankTransaction(getDate(),Constants.Accounts.ACTION_DEPOSIT,account.getAccountId(),amount,account.getBalance());
            long insertedRow = expenseDatabase.expenseDao().addTransaction(transaction);
        });
    }

    private String getDate(){
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    public LiveData<List<TransactionDetailsPojo>> getTransactions(){
        return expenseDatabase.expenseDao().getAllTransactions();
    }
    public int deleteTransaction(BankTransaction bankTransaction){
        return expenseDatabase.expenseDao().deleteTransaction(bankTransaction);
    }

    public int updateBalance(BankAccount bankAccount){
        return expenseDatabase.expenseDao().updateBalance(bankAccount);
    }

}
