package com.pencilbox.user.smartwallet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 4/23/2018.
 */

public class DailyReportService extends JobService {
    private static final String TAG = DailyReportService.class.getSimpleName();
    private static final String CHANNEL_ID = "channel_001";

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.e(TAG, "job started");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String currentDate = sdf.format(new Date());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_report_daily)
                .setContentTitle("Hello from the other world!")
                .setContentText(currentDate)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            manager.createNotificationChannel(channel);
        }
        manager.notify(100,mBuilder.build());

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.e(TAG, "job ended");
        return true;
    }
}
