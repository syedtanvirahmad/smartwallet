package com.pencilbox.user.smartwallet.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by User on 11/21/2017.
 */
@Dao
public interface ExpenseDao {
    @Query("select * from expense order by id desc")
    LiveData<List<Expense>>getAllExpenses();

    @Query("select * from expense")
    List<Expense> getAllExpensesAsList();

    @Query("select * from expense where expense_date_time like:date order by id desc")
    LiveData<List<Expense>>getTodaysExpense(String date);

    @Query("select * from expense where expense_date_time like:date order by id desc")
    LiveData<List<Expense>>getCurrentMonthExpense(String date);

    @Query("select * from expense where expense_date_time like:date order by id desc")
    LiveData<List<Expense>>getSeletectedMonthExpense(String date);

    @Query("select sum(expense_amount) from expense")
    double getGrandTotalExpenses();

    @Insert
    long insertExpense(Expense expenses);

    @Delete
    public void deleteExpense(Expense... expenses);

    @Insert
    long[]insertIncomeSource(IncomeSource... incomeSources);

    @Query("select * from income_source")
    LiveData<List<IncomeSource>>getAllIncomeSources();

    @Query("select organization_name from income_source")
    List<String>getNamesOfAllIncomeSources();

    @Insert
    long[]insertIncome(Income... incomes);

    @Query("select * from income_table")
    LiveData<List<Income>>getAllIncome();

    /*@Query("select * from income_source")
    List<IncomeWithSource>getIncomesWithSource();*/

    @Query("select * from income_table inner join income_source on income_source.id = income_table.income_sourceId")
    LiveData<List<FullIncomeTest>>getAllIncomeTest();

    @Query("select sum(income_amount) from income_table")
    double getGrandTotalIncome();

    @Delete
    int deleteIncome(Income...incomes);

    @Query("select * from income_table inner join income_source on income_source.id = income_table.income_sourceId order by income_table.income_id desc limit 1")
    FullIncomeTest getLastIncome();


    @Query("select expense_date_time from expense where id = (select min(id) from expense)")
    String getFirstDate();
    @Query("select expense_date_time from expense where id = (select max(id) from expense)")
    String getLastDate();

    @Query("select count(distinct expense_date_time) from expense")
    int totalDays();

    @Query("select distinct expense_date_time from expense where expense_date_time like:date")
    List<String>daysOfCurrentMonth(String date);

    @Query("select sum(expense_amount) from expense where expense_date_time like:date")
    double getTotalExpensePerDay(String date);

    @Query("select * from expense where expense_date_time like:date")
    List<Expense>getAllExpensesByMonth(String date);

    @Query("select distinct expense_name from expense where expense_date_time like:date")
        List<String>getDistinctExpenseNamesByMonth(String date);

    @Query("select sum(expense_amount) from expense where expense_name like:name and expense_date_time like:date")
    double getAmountForSpecificExpense(String name, String date);

    @Query("select sum(expense_amount) from expense where expense_date_time like:date")
    double getTotalExpenseByMonth(String date);
}
