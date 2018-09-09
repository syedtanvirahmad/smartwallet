package com.pencilbox.user.smartwallet.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.pencilbox.user.smartwallet.Database.BankAccount;
import com.pencilbox.user.smartwallet.Database.TransactionDetailsPojo;
import com.pencilbox.user.smartwallet.R;
import com.pencilbox.user.smartwallet.ViewModel.AccountViewModel;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{

    private List<TransactionDetailsPojo> transactions;
    private AccountViewModel accountViewModel;
    private Context context;

    public TransactionAdapter(List<TransactionDetailsPojo> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row,parent,false);
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        holder.transactionTypeTV.setText(transactions.get(position).getTransaction_action());
        holder.bankNameTV.setText(transactions.get(position).getBank_name());
        holder.transactionTimeTV.setText(transactions.get(position).getTransaction_date());
        holder.amountTV.setText(String.format("%.1f", transactions.get(position).getTransaction_amount()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView amountTV;
        TextView transactionTypeTV;
        TextView bankNameTV;
        TextView transactionTimeTV;
        public TransactionViewHolder(View itemView) {
            super(itemView);
            amountTV = itemView.findViewById(R.id.row_transactionAmount);
            transactionTypeTV = itemView.findViewById(R.id.row_transactionType);
            bankNameTV = itemView.findViewById(R.id.row_transactionBankName);
            transactionTimeTV = itemView.findViewById(R.id.row_transactionDateTime);
        }
    }

    public void addTransactions(List<TransactionDetailsPojo>transactions){
        this.transactions = transactions;
        notifyDataSetChanged();
    }
}
