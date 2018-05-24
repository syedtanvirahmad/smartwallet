package com.pencilbox.user.smartwallet.Utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ScrollingTabContainerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pencilbox.user.smartwallet.Database.Expense;
import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Database.Income;
import com.pencilbox.user.smartwallet.Database.IncomeSource;
import com.pencilbox.user.smartwallet.ExpenseSuggestion;
import com.pencilbox.user.smartwallet.Interface.AddIncomeListener;
import com.pencilbox.user.smartwallet.Interface.OnAddExpenseFinishedListener;
import com.pencilbox.user.smartwallet.MainActivity;
import com.pencilbox.user.smartwallet.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 11/21/2017.
 */

public final class ExpenseDialog {
    private static ExpenseDatabase expenseDatabase;
    private static String incomeType;
    private static List<IncomeSource>sources;
    private static List<String>namesOfSources;
    private static String incomeSource;
    private static int incomeSourceId;

    public static void createDialogForAddingExpense(final Context context, final OnAddExpenseFinishedListener onAddExpenseFinishedListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        CardView cv = (CardView) LayoutInflater.from(context).inflate(R.layout.add_new_expense_dialog,null);
        final TextInputEditText amountET = cv.findViewById(R.id.expenseAmountET);
        final TextInputEditText expenseNameET = cv.findViewById(R.id.expenseNameET);
        final TextInputEditText expenseDateET = cv.findViewById(R.id.expenseDateTimeET);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateTime = sdf.format(new Date());
        expenseDateET.setText(dateTime);
        expenseDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(context,expenseDateET);
            }
        });
        builder.setView(cv);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                double amount = Double.parseDouble(amountET.getText().toString());
                String name = expenseNameET.getText().toString();
                String dateTime = expenseDateET.getText().toString();
                expenseDatabase = ExpenseDatabase.getInstance(context);
                SearchRecentSuggestions suggestions =
                        new SearchRecentSuggestions(context, ExpenseSuggestion.AUTHORITY,
                                ExpenseSuggestion.MODE);
                suggestions.saveRecentQuery(name,null);
                /*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                String dateTime = sdf.format(new Date());*/
                long inserted = expenseDatabase.expenseDao().insertExpense(new Expense(name,amount,dateTime));
                if(inserted > 0){
                    Toast.makeText(context, "Expense Added", Toast.LENGTH_SHORT).show();
                    onAddExpenseFinishedListener.onAddExpenseFinished();
                }else{
                    Toast.makeText(context, "Failed to add expense.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }
    public static void createDialogForAddingIncomeSource(final Context context, final AddIncomeListener.AddIncomeSourceListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final CardView cv = (CardView) LayoutInflater.from(context).inflate(R.layout.add_income_source_dialog,null);
        final TextInputEditText organizationNameET = cv.findViewById(R.id.organizationNameValue);
        final TextInputEditText incomeExpenseAmountET = cv.findViewById(R.id.incomeAmountValue);
        final RadioGroup incomeTypeRG = cv.findViewById(R.id.incomeTypeRG);
        incomeTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = cv.findViewById(i);
                incomeType = rb.getText().toString();
            }
        });
        builder.setView(cv);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //double amount = Double.parseDouble(incomeExpenseAmountET.getText().toString());
                String orgName = organizationNameET.getText().toString();
                expenseDatabase = ExpenseDatabase.getInstance(context);
                IncomeSource is = new IncomeSource(orgName,incomeType);
                long[]inserted = expenseDatabase.expenseDao().insertIncomeSource(is);
                if(inserted[0] > 0){
                    //Toast.makeText(context, "Income source added", Toast.LENGTH_SHORT).show();
                    listener.onAddIncomeSourceAdded();
                }else{
                    Toast.makeText(context, "Could not save", Toast.LENGTH_SHORT).show();
                }


            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }
    public static void createDialogForAddingIncome(final Context context, final AddIncomeListener addIncomeListener){
        LifecycleOwner lifecycleOwner = (LifecycleOwner) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final CardView cv = (CardView) LayoutInflater.from(context).inflate(R.layout.add_income_dialog,null);
        final TextInputEditText incomeExpenseAmountET = cv.findViewById(R.id.incomeValue);
        final TextInputEditText incomeExpenseDescriptionET = cv.findViewById(R.id.incomeSourceDescription);
        final Spinner sourceSP = cv.findViewById(R.id.incomeSourceSP);
        expenseDatabase = ExpenseDatabase.getInstance(context);
        namesOfSources = expenseDatabase.expenseDao().getNamesOfAllIncomeSources();
        expenseDatabase.expenseDao().getAllIncomeSources().observe(lifecycleOwner, new Observer<List<IncomeSource>>() {
            @Override
            public void onChanged(@Nullable List<IncomeSource> incomeSources) {
                sources = incomeSources;
            }
        });

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,namesOfSources);
        sourceSP.setAdapter(adapter);
        sourceSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                incomeSource = adapterView.getSelectedItem().toString();
                incomeSourceId = sources.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final TextView incomeDateTV = cv.findViewById(R.id.incomeDate);
        incomeDateTV.setText(getCurrentDate());

        builder.setView(cv);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                double amount = Double.parseDouble(incomeExpenseAmountET.getText().toString());
                final String date = incomeDateTV.getText().toString();
                final String description = incomeExpenseDescriptionET.getText().toString();
                Income income = new Income(amount,incomeSourceId,date,description);
                long[]inserted = expenseDatabase.expenseDao().insertIncome(income);
                if(inserted[0] > 0){
                    Toast.makeText(context, "Income added", Toast.LENGTH_SHORT).show();
                    addIncomeListener.onAddIncome();
                }else{
                    Toast.makeText(context, "Failed to add Income", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();
        incomeDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(context,incomeDateTV);
            }
        });
    }

    private static void setDate(Context context, final TextView incomeDateTV) {
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                incomeDateTV.setText(new SimpleDateFormat("dd/MM/yyyy").format(GetDate.getTime(i,i1,i2)));
            }
        }, GetDate.getCalender().get(Calendar.YEAR), GetDate.getCalender().get(Calendar.MONTH), GetDate.getCalender().get(Calendar.DAY_OF_MONTH)).show();
    }

    private static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }

    private static final class GetDate{
        private static Calendar calendar;
        private static int year,month,day;

        public static Calendar getCalender(){
            calendar = Calendar.getInstance();
            return calendar;
        }
        public static Date getTime(int year, int month, int day){
            calendar.set(year,month,day);
            return calendar.getTime();
        }
    }

    public static void createDialogForDepositOrWithdraw(Context context, String scheme, OnUpdateBalanceListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(scheme);
        EditText editText = new AutoCompleteTextView(context);
        editText.setHint(R.string.enter_amount);
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(editText);
        builder.setPositiveButton(scheme, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                double amount = Double.parseDouble(editText.getText().toString());
                listener.onUpdateBalance(amount);
            }
        });
        builder.show();
    }
    public interface OnUpdateBalanceListener{
        void onUpdateBalance(double amount);
    }
}
