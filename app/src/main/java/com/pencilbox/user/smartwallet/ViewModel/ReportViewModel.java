package com.pencilbox.user.smartwallet.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pencilbox.user.smartwallet.Database.Expense;
import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Database.ExpensePerExpenseName;
import com.pencilbox.user.smartwallet.Database.PerDayExpenses;
import com.pencilbox.user.smartwallet.Interface.ExpenseReport;
import com.pencilbox.user.smartwallet.Interface.ExpenseReportImpl;
import com.pencilbox.user.smartwallet.ViewReportActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 4/18/2018.
 */

public class ReportViewModel extends AndroidViewModel {
    private ExpenseDatabase expenseDatabase;
    List<PerDayExpenses>perDayExpenses = new ArrayList<>();
    private Context context;
    private ArrayList<BarEntry> barEntries;
    private List<String>days = new ArrayList<>();
    private List<String>daysOfMonth = new ArrayList<>();
    private BarData data;
    private SimpleDateFormat my;
    private Calendar calendar;
    private String selectedMonth = "";
    private ExpenseReport expenseReport;
    public ReportViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        expenseDatabase = ExpenseDatabase.getInstance(application);
        barEntries = new ArrayList<>();
    }

    private void init(CalendarDay day){
        calendar = day.getCalendar();
        my = new SimpleDateFormat("MM/yyyy");
    }
    private void init(){

    }

    public String getSelectedMonth(CalendarDay date){
        init(date);
        return my.format(calendar.getTime());
    }

    public String getSelectedMonthFormattedText(CalendarDay date){
        init(date);
        return new SimpleDateFormat("MMM yyyy").format(calendar.getTime());
    }
    private List<String> getDaysOfMonth(String month){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        String date = month != null ? "%/"+month : "%/"+sdf.format(new Date());
        //Toast.makeText(context, "date: "+date, Toast.LENGTH_SHORT).show();
        return expenseDatabase.expenseDao().daysOfCurrentMonth(date);
    }

    private double getExpensePerDay(String date){
        return expenseDatabase.expenseDao().getTotalExpensePerDay(date);
    }

    public BarData getBarDataForSelectedMonth(@Nullable String month){
        days.clear();
        daysOfMonth.clear();
        perDayExpenses.clear();
        barEntries.clear();
        days = getDaysOfMonth(month);
        for(String s : days){
            //Log.e(TAG, s );
            double amount = getExpensePerDay(s);
            PerDayExpenses dayExpenses = new PerDayExpenses(s,amount);
            perDayExpenses.add(dayExpenses);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        for(PerDayExpenses p : perDayExpenses){
            try {
                Date d = sdf.parse(p.getExpense_date_time());
                float date = Float.parseFloat(sdf.format(d));
                barEntries.add(new BarEntry(date, (float) p.getTotalExpensePerDay()));
                daysOfMonth.add(sdf.format(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        SimpleDateFormat my = new SimpleDateFormat("MM/yyyy");
        String dateString = "";
        if(month == null){
            dateString = "%/"+my.format(new Date());
        }else{
            dateString = "%/"+month;
        }

        double totalForMonth = expenseDatabase.expenseDao().getTotalExpenseByMonth(dateString);
        String label = String.valueOf("Total: "+totalForMonth);
        BarDataSet dataSet =
                new BarDataSet(barEntries,label);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        data = new BarData(dataSet);
        return data;
    }

    public ViewReportActivity.MyXAxisValueFormatter getValueFormatter(){
        return new ViewReportActivity.MyXAxisValueFormatter(daysOfMonth);
    }
    public List<ExpensePerExpenseName>getExpensesForExpenseNames(@Nullable String month){
        List<ExpensePerExpenseName> lists = new ArrayList<>();
        String date = "";
        if(month == null){
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
            date = "%/"+sdf.format(new Date());
        }else{
            date = "%/"+month;
        }
        List<String>names = expenseDatabase.expenseDao().getDistinctExpenseNamesByMonth(date);
        for(String s : names){
            double amount = expenseDatabase.expenseDao().getAmountForSpecificExpense(s,date);
            String nameCapFirst = s.substring(0,1).toUpperCase()+s.substring(1);
            lists.add(new ExpensePerExpenseName(nameCapFirst,amount));
        }
        return lists;
    }
}
