package com.pencilbox.user.smartwallet;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.pencilbox.user.smartwallet.Utils.Constants;

/**
 * Created by User on 4/15/2018.
 */

public class SmartWalletApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
