package com.pencilbox.user.smartwallet.Utils;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;

import com.pencilbox.user.smartwallet.Database.Expense;
import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Interface.LoadFilteredExpenses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 11/22/2017.
 */

public final class ExpenseFilter {
    private static ExpenseDatabase expenseDatabase;

    public static double getCurrentDayTotalExpense(List<Expense>expenses){
        double total = 0.0;
        for(Expense e : expenses){
            total += e.getExpenseAmount();
        }
        return total;
    }
    public static void getCurrentDayExpenses(Context context, final LoadFilteredExpenses loadFilteredExpenses){
        LifecycleOwner lifecycleOwner = (LifecycleOwner) context;
        expenseDatabase = ExpenseDatabase.getInstance(context);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date())+"%";
        expenseDatabase.expenseDao().getTodaysExpense(date).observe(lifecycleOwner, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                //loadFilteredExpenses.onLoadCurrentDayExpenses(expenses);
            }
        });
    }
}
