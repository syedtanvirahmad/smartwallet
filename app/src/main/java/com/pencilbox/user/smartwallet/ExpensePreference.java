package com.pencilbox.user.smartwallet;

import android.content.Context;
import android.content.SharedPreferences;

import com.pencilbox.user.smartwallet.Utils.Constants;

public class ExpensePreference {
    private static final String USER_EMAIL = "email_address";
    private static final String USER_PASSWORD = "password";
    private static final String IS_LOGGED_IN = "password";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public ExpensePreference(Context context){
        preferences = context.getSharedPreferences(Constants.PREFERENCE,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void setStatus(boolean status){
        editor.putBoolean(IS_LOGGED_IN,true);
    }
    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGGED_IN,false);
    }
}
