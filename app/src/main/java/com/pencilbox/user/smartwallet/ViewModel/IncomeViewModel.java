package com.pencilbox.user.smartwallet.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Database.FullIncomeTest;
import com.pencilbox.user.smartwallet.Database.Income;
import com.pencilbox.user.smartwallet.Database.IncomeWithSource;

import java.util.List;

/**
 * Created by User on 12/2/2017.
 */

public class IncomeViewModel extends AndroidViewModel {
    private ExpenseDatabase expenseDatabase;
    private LiveData<List<Income>>incomes;
    public IncomeViewModel(@NonNull Application application) {
        super(application);
        expenseDatabase = ExpenseDatabase.getInstance(application);
    }
    public LiveData<List<Income>>getAllIncome(){
        return expenseDatabase.expenseDao().getAllIncome();
    }

    public LiveData<List<FullIncomeTest>>getAllIncomesTest(){
        return expenseDatabase.expenseDao().getAllIncomeTest();
    }
    public double getGrandIncome(){
        return expenseDatabase.expenseDao().getGrandTotalIncome();
    }
}
