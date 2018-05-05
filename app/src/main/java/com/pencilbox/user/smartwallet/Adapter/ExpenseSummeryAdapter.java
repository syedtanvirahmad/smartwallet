package com.pencilbox.user.smartwallet.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pencilbox.user.smartwallet.Database.ExpensePerExpenseName;
import com.pencilbox.user.smartwallet.R;

import java.util.List;

/**
 * Created by User on 5/5/2018.
 */

public class ExpenseSummeryAdapter extends RecyclerView.Adapter<ExpenseSummeryAdapter.SummeryViewHolder> {
    private List<ExpensePerExpenseName> lists;

    public ExpenseSummeryAdapter(List<ExpensePerExpenseName> lists) {
        this.lists = lists;
    }

    @Override
    public SummeryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_name_row,parent,false);
        return new SummeryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SummeryViewHolder holder, int position) {
        holder.expNameTV.setText(lists.get(position).getExpense_name());
        holder.expAmountTV.setText(String.valueOf(lists.get(position).getExpense_amount()));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class SummeryViewHolder extends RecyclerView.ViewHolder{
        TextView expNameTV, expAmountTV;
        public SummeryViewHolder(View itemView) {
            super(itemView);
            expNameTV = itemView.findViewById(R.id.row_expense_name);
            expAmountTV = itemView.findViewById(R.id.row_expense_total);
        }
    }
}
