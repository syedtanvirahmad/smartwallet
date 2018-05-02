package com.pencilbox.user.smartwallet.Interface;

import android.content.Context;

import com.pencilbox.user.smartwallet.Database.Expense;

import java.util.List;

/**
 * Created by User on 4/10/2018.
 */

public interface MainView {
    void onSetDateListener(String date);
    interface ExpenseDateListener{
        void onAddExpenseDate(Context context);
    }
    interface ExpenseAmountListener{
        void totalExpenseAmount(List<Expense>expenses);
    }
}
