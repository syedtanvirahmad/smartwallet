package com.pencilbox.user.smartwallet.Database;

/**
 * Created by User on 4/28/2018.
 */

public class ExpensePerExpenseName {
    private String expense_name;
    private double expense_amount;

    public ExpensePerExpenseName(String expense_name, double expense_amount) {
        this.expense_name = expense_name;
        this.expense_amount = expense_amount;
    }

    public String getExpense_name() {
        return expense_name;
    }

    public double getExpense_amount() {
        return expense_amount;
    }
}
