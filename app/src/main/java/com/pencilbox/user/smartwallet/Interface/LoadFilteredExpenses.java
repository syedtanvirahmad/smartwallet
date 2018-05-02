package com.pencilbox.user.smartwallet.Interface;

import android.arch.lifecycle.LiveData;

import com.pencilbox.user.smartwallet.Database.Expense;

import java.util.List;

/**
 * Created by User on 11/22/2017.
 */

public interface LoadFilteredExpenses {
    void onLoadCurrentDayExpenses(LiveData<List<Expense>>expenses);
}
