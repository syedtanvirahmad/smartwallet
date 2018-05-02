package com.pencilbox.user.smartwallet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.pencilbox.user.smartwallet.Adapter.IncomeAdapter;
import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Database.FullIncome;
import com.pencilbox.user.smartwallet.Database.FullIncomeTest;
import com.pencilbox.user.smartwallet.Database.Income;
import com.pencilbox.user.smartwallet.Database.IncomeWithSource;
import com.pencilbox.user.smartwallet.ViewModel.IncomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShowIncomeActivity extends AppCompatActivity {
    private static final String TAG = ShowIncomeActivity.class.getSimpleName();
    private RecyclerView incomeRV;
    private IncomeViewModel incomeViewModel;
    private IncomeAdapter incomeAdapter;
    private List<FullIncomeTest>incomes;
    private LinearLayoutManager llm;
    private ExpenseDatabase expenseDatabase;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_income);
        incomeRV = findViewById(R.id.incomeRV);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        expenseDatabase = ExpenseDatabase.getInstance(this);
        incomes = new ArrayList<>();
        incomeAdapter = new IncomeAdapter(this,incomes);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        incomeRV.setLayoutManager(llm);
        incomeRV.setAdapter(incomeAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(incomeRV);
        incomeViewModel = ViewModelProviders.of(this).get(IncomeViewModel.class);
        //List<IncomeWithSource>incomeWithSources = incomeViewModel.getIncomesWithSource();
        incomeViewModel.getAllIncomesTest().observe(this, new Observer<List<FullIncomeTest>>() {
            @Override
            public void onChanged(@Nullable List<FullIncomeTest> fullIncomeTests) {
                /*for(FullIncomeTest ft : fullIncomeTests){
                    Log.e(TAG, "amount:"+ft.income_amount+ " "+ft.organization_name+" "+ft.income_date);
                }*/
                incomeAdapter.addIncomeItems(fullIncomeTests);
            }
        });
    }
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            FullIncomeTest fullIncomeTest = incomeAdapter.getIncomes().get(position);
            Income income = new Income(fullIncomeTest.income_id,fullIncomeTest.income_amount,fullIncomeTest.income_sourceId,fullIncomeTest.income_date,fullIncomeTest.source_description);
            int deleted = expenseDatabase.expenseDao().deleteIncome(income);
            if(deleted > 0){
                Toast.makeText(ShowIncomeActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                List<FullIncomeTest>loadedIncomes = incomeAdapter.getIncomes();
                loadedIncomes.remove(position);
                incomeAdapter.addIncomeItems(loadedIncomes);
            }else{
                Toast.makeText(ShowIncomeActivity.this, "Could not delete", Toast.LENGTH_SHORT).show();
                Toast.makeText(ShowIncomeActivity.this, ""+fullIncomeTest.income_id, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
