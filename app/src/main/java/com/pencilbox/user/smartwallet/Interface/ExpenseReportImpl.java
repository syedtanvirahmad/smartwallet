package com.pencilbox.user.smartwallet.Interface;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.pencilbox.user.smartwallet.ViewModel.ReportViewModel;

/**
 * Created by User on 5/9/2018.
 */

public class ExpenseReportImpl implements ExpenseReport {

    private ReportViewModel reportViewModel;
    private BarData barData;

    public ExpenseReportImpl(FragmentActivity activity){
        reportViewModel = ViewModelProviders.of(activity).get(ReportViewModel.class);
    }
    @Override
    public void getBarChartData(String month) {
        barData = reportViewModel.getBarDataForSelectedMonth(month);
    }
}
