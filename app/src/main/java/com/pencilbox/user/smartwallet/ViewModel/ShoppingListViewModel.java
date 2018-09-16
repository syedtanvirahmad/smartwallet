package com.pencilbox.user.smartwallet.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Database.Shopping;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {
    private Context context;
    private LiveData<List<Shopping>>shoppingList;
    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        shoppingList = ExpenseDatabase.getInstance(context)
                .expenseDao()
                .getShoppingList();
    }

    public LiveData<List<Shopping>> getShoppingList() {
        return shoppingList;
    }
}
