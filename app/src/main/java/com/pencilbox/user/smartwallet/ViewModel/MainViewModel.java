package com.pencilbox.user.smartwallet.ViewModel;

import android.app.Application;
import android.app.DatePickerDialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pencilbox.user.smartwallet.Database.Expense;
import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 11/26/2017.
 */

public class MainViewModel extends AndroidViewModel {

    private LiveData<Expense> expenseLiveData;
    private LiveData<List<Expense>> expenses;
    private LiveData<List<Expense>> todayExpenses;
    private ExpenseDatabase expenseDatabase;
    private Context context;
    private DatabaseReference rootRef;
    private DatabaseReference expenseRef;

    public MainViewModel(@NonNull Application application) {
        super(application);
        expenseDatabase = ExpenseDatabase.getInstance(application);
        this.context = application;
        rootRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_ROOT_REFERENCE);
        expenseRef = rootRef.child("expenses");
        expenseRef.keepSynced(true);
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Expense>> getAllExpenses() {
        expenses = expenseDatabase.expenseDao().getAllExpenses();

        return expenses;
    }

    public void uploadAllExpenses(){
        List<Expense> expenseList = expenseDatabase.expenseDao().getAllExpensesAsList();
        for(Expense e : expenseList){
            insertNewExpenseToCloud(e,e.getId());
        }
    }

    public LiveData<List<Expense>> getCurrentDayExpense() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date()) + "%";
        todayExpenses = expenseDatabase.expenseDao().getTodaysExpense(date);
        return todayExpenses;
    }

    public LiveData<List<Expense>> getCurrentMonthExpense() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        String date = "%/"+sdf.format(new Date());
        todayExpenses = expenseDatabase.expenseDao().getCurrentMonthExpense(date);
        return todayExpenses;
    }

    public void deleteSpecificExpense(Expense expense) {
        expenseDatabase.expenseDao().deleteExpense(expense);
    }

    public double getTotalExpenseAmount(List<Expense> expenses) {
        double total = 0.0;
        for (Expense e : expenses) {
            total += e.getExpenseAmount();
        }
        return total;
    }

    public boolean insertNewExpense(Expense expense) {
        long insertedRow = expenseDatabase.expenseDao().insertExpense(expense);
        if (insertedRow > 0) {
            expense.setId((int) insertedRow);
            insertNewExpenseToCloud(expense, insertedRow);
            return true;
        }
        return false;
    }

    public void insertNewExpenseToCloud(Expense expense, long rowId){
        //String key = expenseRef.push().getKey();
        expenseRef.child(String.valueOf(rowId)).setValue(expense);
    }

    public void deleteSpecificExpenseFromCloud(Expense expense){
        expenseRef.child(String.valueOf(expense.getId())).removeValue();
    }

    public void setDate(final TextView tv) {
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                tv.setText(new SimpleDateFormat("dd/MM/yyyy").format(GetDate.getTime(i, i1, i2)));
            }
        }, GetDate.getCalender().get(Calendar.YEAR), GetDate.getCalender().get(Calendar.MONTH), GetDate.getCalender().get(Calendar.DAY_OF_MONTH)).show();
    }


    private static final class GetDate {
        private static Calendar calendar;
        private static int year, month, day;

        public static Calendar getCalender() {
            calendar = Calendar.getInstance();
            return calendar;
        }

        public static Date getTime(int year, int month, int day) {
            calendar.set(year, month, day);
            return calendar.getTime();
        }
    }
}
