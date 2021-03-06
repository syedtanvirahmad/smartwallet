package com.pencilbox.user.smartwallet.Adapter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.pencilbox.user.smartwallet.Database.BankAccount;
import com.pencilbox.user.smartwallet.R;
import com.pencilbox.user.smartwallet.ViewModel.AccountViewModel;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder>{

    private List<BankAccount>accounts;
    private AccountViewModel accountViewModel;
    private Context context;
    private MyAccounts myAccounts;

    public AccountAdapter(List<BankAccount> accounts, Context context) {
        this.accounts = accounts;
        this.context = context;
        myAccounts = (MyAccounts) context;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_row,parent,false);
        return new AccountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        holder.bankNameTV.setText(accounts.get(position).getBankName());
        holder.accNoTV.setText(accounts.get(position).getAccountNo());
        holder.accBalanceTV.setText(String.format("%.1f",accounts.get(position).getBalance()));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder{
        TextView bankNameTV;
        TextView accNoTV;
        TextView accBalanceTV;
        ImageView optionsTV;
        public AccountViewHolder(View itemView) {
            super(itemView);
            bankNameTV = itemView.findViewById(R.id.row_bank_name);
            accNoTV = itemView.findViewById(R.id.row_bank_accountNo);
            accBalanceTV = itemView.findViewById(R.id.row_bank_balance);
            optionsTV = itemView.findViewById(R.id.row_acc_options);
            optionsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    PopupMenu popupMenu = new PopupMenu(view.getContext(),optionsTV);
                    popupMenu.inflate(R.menu.account_popup);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            BankAccount account = accounts.get(position);
                            switch (menuItem.getItemId()){
                                case R.id.deposit:
                                    myAccounts.deposit(account);
                                    break;
                                case R.id.withdraw:
                                    myAccounts.withdraw(account);
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }

    public void addBankAccounts(List<BankAccount>accounts){
        this.accounts = accounts;
        notifyDataSetChanged();
    }
    public interface MyAccounts{
        void deposit(BankAccount account);
        void withdraw(BankAccount account);
    }
}
