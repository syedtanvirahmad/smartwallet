package com.pencilbox.user.smartwallet.Interface;

import android.support.annotation.Nullable;

/**
 * Created by User on 5/1/2018.
 */

public interface ExpenseReport {
    void getBarChartData(String month);
    interface SummeryReport{
        void getSummeryData(String month);
    }
}
