package com.pencilbox.user.smartwallet.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Database.FullIncomeTest;
import com.pencilbox.user.smartwallet.Database.Income;
import com.pencilbox.user.smartwallet.Database.IncomeSource;

import java.util.List;

/**
 * Created by User on 11/30/2017.
 */

public class DashboardViewModel extends AndroidViewModel {
    private ExpenseDatabase expenseDatabase;
    private LiveData<List<IncomeSource>>incomeSources;
    public DashboardViewModel(@NonNull Application application) {
        super(application);
        expenseDatabase = ExpenseDatabase.getInstance(application);
    }

    public FullIncomeTest getLastIncome(){
        return expenseDatabase.expenseDao().getLastIncome();
    }
    public double getGrandExpense(){
        return expenseDatabase.expenseDao().getGrandTotalExpenses();
    }
    public String getFirstDate(){
        return expenseDatabase.expenseDao().getFirstDate();
    }
    public String getLastDate(){
        return expenseDatabase.expenseDao().getLastDate();
    }
    public int getTotalDays(){
        return expenseDatabase.expenseDao().totalDays();
    }
    public double getGrandIncome(){
        return expenseDatabase.expenseDao().getGrandTotalIncome();
    }

}
