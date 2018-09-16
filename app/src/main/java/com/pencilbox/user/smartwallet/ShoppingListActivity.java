package com.pencilbox.user.smartwallet;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pencilbox.user.smartwallet.ViewModel.ShoppingListViewModel;

public class ShoppingListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ShoppingListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        viewModel = ViewModelProviders.of(this).get(ShoppingListViewModel.class);
    }
}
