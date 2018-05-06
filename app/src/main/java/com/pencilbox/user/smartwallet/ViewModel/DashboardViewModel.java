package com.pencilbox.user.smartwallet.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.ArraySet;
import android.util.Log;

import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Database.FullIncomeTest;
import com.pencilbox.user.smartwallet.Database.Income;
import com.pencilbox.user.smartwallet.Database.IncomeSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by User on 11/30/2017.
 */

public class DashboardViewModel extends AndroidViewModel {
    private ExpenseDatabase expenseDatabase;
    private LiveData<List<IncomeSource>>incomeSources;
    private static final String TAG = DashboardViewModel.class.getSimpleName();
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
    public List<String>getIncomeDates(){
        List<String> dates = expenseDatabase.expenseDao().getDistinctIncomeMonths();
        /*Set<String> months = new HashSet<>();
        for(String st : dates){
            String n = st.substring(3);
            Log.e(TAG, "getIncomeDates: "+n);
            months.add(n);
        }
        for(String s : dates){
            total.add(expenseDatabase.expenseDao().getTotalIncomeByDate(s));
        }*/
        return dates;
    }


}
