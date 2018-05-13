package com.pencilbox.user.smartwallet.Interface;

import android.support.annotation.Nullable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * Created by User on 5/1/2018.
 */

public interface ExpenseReport {
    void getBarChartData(String month);
    interface SummeryReport{
        void getSummeryData(@Nullable String month, @Nullable CalendarDay date);
    }
}
