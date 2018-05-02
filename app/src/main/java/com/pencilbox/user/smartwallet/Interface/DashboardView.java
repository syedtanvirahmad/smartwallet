package com.pencilbox.user.smartwallet.Interface;

/**
 * Created by User on 4/4/2018.
 */

public interface DashboardView {
    void setAvgDailyExpense(double expense);
    void setAvgMonthlyExpense(double expense);
    void setCurrentBalance(double amount);
    public interface ValueAverageCalculate{
        void getAvgExpenseDaily(String date1, String date2, double expense);
        void getAvgExpensePerDay(int totalDay, double expense);
        void getAvgExpensePerMonth(int totalDay, double expense);
        void getCurrentBalance(double income, double expense);
    }
}
