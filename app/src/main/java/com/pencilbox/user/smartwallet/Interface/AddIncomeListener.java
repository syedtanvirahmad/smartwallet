package com.pencilbox.user.smartwallet.Interface;

/**
 * Created by User on 11/27/2017.
 */

public interface AddIncomeListener {
    void onAddIncome();
    interface AddIncomeSourceListener{
        void onAddIncomeSourceAdded();
    }
}
