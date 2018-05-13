package com.pencilbox.user.smartwallet;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pencilbox.user.smartwallet.Database.ExpensePerExpenseName;
import com.pencilbox.user.smartwallet.Interface.ExpenseReport;
import com.pencilbox.user.smartwallet.ViewModel.ReportViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummeryReportFragment2 extends Fragment implements ExpenseReport.SummeryReport{
    private ReportViewModel reportViewModel;
    private static final String TAG = SummeryReportFragment2.class.getSimpleName();
    private TextView totalExpTV, avgExpTV, projectedExpTV, totalIncomeTV;
    private String selectedMonth = null;
    private static final String SELECTED_MONTH = "month";
    private CalendarDay calendarDay;
    private int today = 0;
    private int currentMonth = 0;

    public SummeryReportFragment2() {
        // Required empty public constructor
    }

    public static SummeryReportFragment2 getInstance(CalendarDay date){
        SummeryReportFragment2 fragment = new SummeryReportFragment2();
        Bundle b = new Bundle();
        b.putParcelable("date",date);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(savedInstanceState != null){
            selectedMonth = savedInstanceState.getString(SELECTED_MONTH);
        }
        try{
            calendarDay = getArguments().getParcelable("date");
            today = calendarDay.getDay();
            currentMonth = calendarDay.getMonth();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_summery_report2, container, false);
        totalExpTV = v.findViewById(R.id.tRow_totalExpense);
        avgExpTV = v.findViewById(R.id.tRow_avgExpense);
        projectedExpTV = v.findViewById(R.id.tRow_projectedExpense);
        totalIncomeTV = v.findViewById(R.id.tRow_totalIncome);
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        getSummeryData(selectedMonth,calendarDay);
        return v;
    }

    @Override
    public void getSummeryData(String month, CalendarDay date) {
        int selectedDay = 0;
        int currentDay = today;
        int currentMonth = 0;
        int totalDaysInMonth = 0;
        Calendar calendar = Calendar.getInstance();
        Calendar calendarDate = date.getCalendar();
        totalDaysInMonth = calendarDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        Log.e(TAG, "getDay(): "+date.getDay());
        Log.e(TAG, "today: "+today);
        Log.e(TAG, "current month: "+currentMonth);
        if(date.getMonth() == currentMonth){
            currentDay = today;
        }else{
            currentDay = calendarDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if(selectedMonth == null){
            selectedMonth = month;
        }
        List<ExpensePerExpenseName>lists = reportViewModel.getExpensesForExpenseNames(month);
        double totalExp = reportViewModel.getTotalExpenseByMonth(month);
        double projectedExp = (totalExp * totalDaysInMonth) / currentDay;
        double avgExp = totalExp / currentDay;
        totalExpTV.setText(String.valueOf(totalExp));
        projectedExpTV.setText(String.format("%.1f",projectedExp));
        avgExpTV.setText(String.format("%.1f",avgExp));
        Log.e(TAG, "current day: "+currentDay);
        totalIncomeTV.setText(String.format("%.1f",reportViewModel.getIncomeByMonth(month)));

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SELECTED_MONTH,selectedMonth);
    }
}
