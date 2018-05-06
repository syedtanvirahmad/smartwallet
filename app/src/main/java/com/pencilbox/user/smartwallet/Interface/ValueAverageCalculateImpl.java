package com.pencilbox.user.smartwallet.Interface;

import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 4/4/2018.
 */

public class ValueAverageCalculateImpl implements DashboardView.ValueAverageCalculate {
    private static final String TAG = ValueAverageCalculateImpl.class.getSimpleName();
    private DashboardView view;
    public ValueAverageCalculateImpl(DashboardView view){
        if(view != null){
            this.view = view;
        }
    }
    @Override
    public void getAvgExpenseDaily(String date1, String date2, double expense) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        double amount;
        try {
            Date first = sdf.parse(date1);
            Date last = sdf.parse(date2);
            long diff = Math.abs(last.getTime() - first.getTime());
            int days = (int) TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
            Log.e(TAG, "getAvgExpenseDaily: "+days);
            if(days == 0 || days == 1){
                amount = expense;
            }else{
                amount = expense / days;
            }
            view.setAvgDailyExpense(amount);

        } catch (ParseException e) {
            Log.e(TAG, "getAvgExpenseDaily: "+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void getAvgExpensePerDay(int totalDay, double expense) {
        double amount = 0.0;
        if(totalDay == 0 || totalDay == 1){
            amount = expense;
        }else{
            amount = expense / totalDay;
        }
        view.setAvgDailyExpense(amount);
    }

    @Override
    public void getAvgExpensePerMonth(int totalDay, double expense) {
        double amount = 0.0;
        if(totalDay == 0 || totalDay == 1){
            amount = expense;
        }else{
            amount = (expense *30) / totalDay;
        }
        view.setAvgMonthlyExpense(amount);
    }

    @Override
    public void getCurrentBalance(double income, double expense) {
        view.setCurrentBalance(income - expense);
    }

    @Override
    public void getAvgIncomePerMonth(double income, List<String>dates) {
        Set<String> months = new HashSet<>();
        for(String st : dates){
            String n = st.substring(3);
            months.add(n);
        }
        view.setAvgMonthlyIncome(income / months.size());
    }
}
