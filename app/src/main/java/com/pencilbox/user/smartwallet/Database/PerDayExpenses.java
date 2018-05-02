package com.pencilbox.user.smartwallet.Database;

/**
 * Created by User on 4/18/2018.
 */

public class PerDayExpenses {
    private String expense_date_time;
    private double totalExpensePerDay;

    public PerDayExpenses(String expense_date_time, double totalExpensePerDay) {
        this.expense_date_time = expense_date_time;
        this.totalExpensePerDay = totalExpensePerDay;
    }

    public String getExpense_date_time() {
        return expense_date_time;
    }

    public double getTotalExpensePerDay() {
        return totalExpensePerDay;
    }
}
