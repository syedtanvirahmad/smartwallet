package com.pencilbox.user.smartwallet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_DATE_CHANGED)){
            Log.e("test", "date changed");
            context.startService(new Intent(context,MyIntentService.class));
        }
    }
}
