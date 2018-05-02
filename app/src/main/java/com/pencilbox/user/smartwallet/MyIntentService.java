package com.pencilbox.user.smartwallet;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MyIntentService extends IntentService {
    private ExpenseDatabase db;
    private SimpleDateFormat sdf;
    private Calendar calendar;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = ExpenseDatabase.getInstance(this);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        calendar = Calendar.getInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_report_daily);
        builder.setContentTitle("date changed");
        builder.setContentText("click to view report");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(11,builder.build());
    }

}
