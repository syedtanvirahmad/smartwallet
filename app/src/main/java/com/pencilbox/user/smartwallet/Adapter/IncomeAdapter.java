package com.pencilbox.user.smartwallet.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pencilbox.user.smartwallet.Database.FullIncomeTest;
import com.pencilbox.user.smartwallet.Database.Income;
import com.pencilbox.user.smartwallet.R;

import java.util.List;

/**
 * Created by User on 11/21/2017.
 */

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
    private Context context;
    private List<FullIncomeTest> incomes;

    public IncomeAdapter(Context context, List<FullIncomeTest> incomes){
        this.context = context;
        this.incomes = incomes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.expense_single_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.incomeAmountTV.setText(String.valueOf(incomes.get(position).income_amount));
        holder.incomeSourceTV.setText(incomes.get(position).organization_name);
        holder.incomeDateTV.setText(incomes.get(position).income_date);
    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }

    public void addIncomeItems(List<FullIncomeTest>incomes){
        this.incomes = incomes;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView incomeAmountTV;
        TextView incomeSourceTV;
        TextView incomeDateTV;

        public ViewHolder(View itemView) {
            super(itemView);
            incomeAmountTV = itemView.findViewById(R.id.dailyExpenseAmount);
            incomeSourceTV = itemView.findViewById(R.id.dailyExpenseType);
            incomeDateTV = itemView.findViewById(R.id.dailyExpenseDateTime);
        }
    }
    public List<FullIncomeTest> getIncomes(){
        return incomes;
    }
}
