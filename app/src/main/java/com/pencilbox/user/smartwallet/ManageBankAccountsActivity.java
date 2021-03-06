package com.pencilbox.user.smartwallet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pencilbox.user.smartwallet.Adapter.AccountAdapter;
import com.pencilbox.user.smartwallet.Adapter.TransactionAdapter;
import com.pencilbox.user.smartwallet.Database.BankAccount;
import com.pencilbox.user.smartwallet.Database.BankTransaction;
import com.pencilbox.user.smartwallet.Database.TransactionDetailsPojo;
import com.pencilbox.user.smartwallet.ViewModel.AccountViewModel;

import java.util.List;

public class ManageBankAccountsActivity extends AppCompatActivity implements AccountAdapter.MyAccounts {
    private static final String TAG = ManageBankAccountsActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView accountRV;
    private RecyclerView transactionRV;
    private AccountAdapter adapter;
    private AccountViewModel accountViewModel;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bottomSheetRoot;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bank_accounts);
        bottomSheetRoot = findViewById(R.id.bottomSheetTransaction);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetRoot);
        toolbar = findViewById(R.id.accountsToolbar);
        accountRV = findViewById(R.id.accountRV);
        transactionRV = bottomSheetRoot.findViewById(R.id.transactionRV);
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        setUpActionBar();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        accountRV.setLayoutManager(llm);
        /*List<BankAccount> accounts = accountViewModel.getBankAccounts();
        BankAccount b = accounts.get(0);
        b.setBalance(58723.4);
        accountViewModel.updateBalance(b);*/
        accountViewModel.getBankAccounts().observe(this, new Observer<List<BankAccount>>() {
            @Override
            public void onChanged(@Nullable List<BankAccount> bankAccounts) {

                if(adapter == null){
                    adapter = new AccountAdapter(bankAccounts,ManageBankAccountsActivity.this);
                    accountRV.setAdapter(adapter);
                }else{
                    adapter.addBankAccounts(bankAccounts);
                }

            }
        });
        accountViewModel.getTransactions().observe(this, new Observer<List<TransactionDetailsPojo>>() {
            @Override
            public void onChanged(@Nullable List<TransactionDetailsPojo> transactionDetailsPojos) {
                updateBottomSheetContent(transactionDetailsPojos);
            }
        });

    }

    private void updateBottomSheetContent(List<TransactionDetailsPojo>transactionDetailsPojos) {
        if(transactionAdapter != null){
            transactionAdapter.addTransactions(transactionDetailsPojos);
            return;
        }
        transactionAdapter = new TransactionAdapter(transactionDetailsPojos,this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        transactionRV.setLayoutManager(llm);
        transactionRV.setAdapter(transactionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addAccount:
                showAddAccountDialog();
                break;
            case R.id.showTransactions:
                showAddAccountDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.add_bank_account,null);
        final TextInputEditText bankNameET = v.findViewById(R.id.add_bankName);
        final TextInputEditText bankAccountNameET = v.findViewById(R.id.add_bankAccountName);
        final TextInputEditText bankAccountNoET = v.findViewById(R.id.add_bankAccountNumber);
        final TextInputEditText bankBalanceET = v.findViewById(R.id.add_bankAccCurrentBalance);
        final Button button = v.findViewById(R.id.addBankBtn);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bankName = bankNameET.getText().toString();
                String accountName = bankAccountNameET.getText().toString();
                String accountNo = bankAccountNoET.getText().toString();
                String balance = bankBalanceET.getText().toString();
                if(validateInputField(bankName,accountName,accountNo,balance)){
                    BankAccount ba = new BankAccount(bankName,accountName,accountNo,Double.parseDouble(balance));
                    boolean status = accountViewModel.addBankAccount(ba);
                    if(status){
                        Toast.makeText(ManageBankAccountsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(ManageBankAccountsActivity.this, "Failed to save", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ManageBankAccountsActivity.this, "Please fill all the input fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();

    }

    private void setUpActionBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }
    private boolean validateInputField(String bankName, String accountName, String accountNo, String balance) {

        if(bankName.isEmpty() || accountName.isEmpty() || accountNo.isEmpty() || balance.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public void deposit(BankAccount account) {
        accountViewModel.deposit(account,this);
    }

    @Override
    public void withdraw(BankAccount account) {
        accountViewModel.withdraw(account,this);
    }

    private void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}
