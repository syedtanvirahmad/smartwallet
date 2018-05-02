package com.pencilbox.user.smartwallet.Interface;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import com.pencilbox.user.smartwallet.Utils.AddExpenseDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 4/10/2018.
 */

public class MainViewImpl implements MainView.ExpenseDateListener{
    private MainView mainView;

    public MainViewImpl(MainView mainView){
        if(mainView != null){
            this.mainView = mainView;
        }
    }
    @Override
    public void onAddExpenseDate(Context context) {
        setDate(context);
    }
    private void setDate(Context context) {
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String date = new SimpleDateFormat("dd/MM/yyyy").format(GetDate.getTime(i,i1,i2));
                mainView.onSetDateListener(date);
            }
        }, GetDate.getCalender().get(Calendar.YEAR), GetDate.getCalender().get(Calendar.MONTH), GetDate.getCalender().get(Calendar.DAY_OF_MONTH)).show();
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
}
