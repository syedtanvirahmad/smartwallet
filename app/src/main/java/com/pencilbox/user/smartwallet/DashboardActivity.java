package com.pencilbox.user.smartwallet;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.pencilbox.user.smartwallet.Database.FullIncomeTest;
import com.pencilbox.user.smartwallet.Interface.AddIncomeListener;
import com.pencilbox.user.smartwallet.Interface.DashboardView;
import com.pencilbox.user.smartwallet.Interface.ValueAverageCalculateImpl;
import com.pencilbox.user.smartwallet.Utils.AddExpenseDialog;
import com.pencilbox.user.smartwallet.ViewModel.DashboardViewModel;
import com.pencilbox.user.smartwallet.ViewModel.IncomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements DashboardView{

    private android.support.v7.widget.Toolbar toolbar;
    private DashboardViewModel dashboardViewModel;
    private IncomeViewModel incomeViewModel;
    private TextView lastIncomeTV, grandTotalIncomeTV, grandTotalExpenseTV, currentBalanceTV, avgExpenseMonthTV;
    private TextView avgIncomeMonthTV, avgExpenseDayTV;
    private ValueAverageCalculate valueAverageCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        lastIncomeTV = findViewById(R.id.lastIncomeValue);
        grandTotalIncomeTV = findViewById(R.id.grandTotalIncome);
        grandTotalExpenseTV = findViewById(R.id.grandTotalExpense);
        currentBalanceTV = findViewById(R.id.currentBalanceValue);
        avgExpenseMonthTV = findViewById(R.id.averageExpense);
        avgIncomeMonthTV = findViewById(R.id.averageIncomeMonth);
        avgExpenseDayTV = findViewById(R.id.averageExpenseDay);
        valueAverageCalculate = new ValueAverageCalculateImpl(this);
        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        incomeViewModel = ViewModelProviders.of(this).get(IncomeViewModel.class);
        FullIncomeTest fullIncomeTest = dashboardViewModel.getLastIncome();
        if(fullIncomeTest != null){
            lastIncomeTV.setText("Last Income added on "+fullIncomeTest.income_date+" with amount TK."+fullIncomeTest.income_amount+" from "+fullIncomeTest.organization_name);
        }else{
            lastIncomeTV.setText("No record for income found.");
        }
        grandTotalIncomeTV.setText(String.valueOf(incomeViewModel.getGrandIncome()));
        grandTotalExpenseTV.setText(String.valueOf(dashboardViewModel.getGrandExpense()));
        valueAverageCalculate.getAvgExpensePerDay(dashboardViewModel.getTotalDays(),dashboardViewModel.getGrandExpense());
        valueAverageCalculate.getAvgExpensePerMonth(dashboardViewModel.getTotalDays(),dashboardViewModel.getGrandExpense());
        valueAverageCalculate.getCurrentBalance(dashboardViewModel.getGrandIncome(),dashboardViewModel.getGrandExpense());
        valueAverageCalculate.getAvgIncomePerMonth(dashboardViewModel.getGrandIncome(),dashboardViewModel.getIncomeDates());

    }

    @Override
    public void setAvgDailyExpense(double expense) {
        avgExpenseDayTV.setText(String.format("%.1f",expense));
    }

    @Override
    public void setAvgMonthlyExpense(double expense) {
        avgExpenseMonthTV.setText(String.format("%.1f",expense));
    }

    @Override
    public void setCurrentBalance(double amount) {
        currentBalanceTV.setText(String.valueOf(amount));
    }

    @Override
    public void setAvgMonthlyIncome(double amount) {
        avgIncomeMonthTV.setText(String.valueOf(amount));
    }
}
