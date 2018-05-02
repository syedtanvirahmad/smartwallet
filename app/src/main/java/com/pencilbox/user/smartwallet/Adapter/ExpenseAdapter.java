package com.pencilbox.user.smartwallet.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pencilbox.user.smartwallet.Database.Expense;
import com.pencilbox.user.smartwallet.Interface.MainView;
import com.pencilbox.user.smartwallet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/21/2017.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> implements Filterable{
    private Context context;
    private List<Expense> expenses;
    private List<Expense> filteredExpenses;
    private MainView.ExpenseAmountListener expenseAmountListener;
    public ExpenseAdapter(Context context, List<Expense>expenses){
        this.context = context;
        this.expenses = expenses;
        this.filteredExpenses = expenses;
        expenseAmountListener = (MainView.ExpenseAmountListener) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.expense_single_row_alt,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.expenseAmountTV.setText(String.valueOf(filteredExpenses.get(position).getExpenseAmount()));
        holder.expenseNameTV.setText(filteredExpenses.get(position).getExpenseName());
        holder.expenseDateTimeTV.setText(filteredExpenses.get(position).getExpenseDateTime());
    }

    @Override
    public int getItemCount() {
        return filteredExpenses.size();
    }

    public void addExpenseItems(List<Expense>expenses){
        this.expenses = expenses;
        this.filteredExpenses = expenses;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchString = charSequence.toString();
                if(searchString.isEmpty()){
                    filteredExpenses = expenses;
                }else{
                    List<Expense>tempList = new ArrayList<>();
                    for(Expense e : expenses){
                        if(e.getExpenseName().toLowerCase().contains(searchString.toLowerCase()) ||
                                e.getExpenseDateTime().contains(charSequence)){
                            tempList.add(e);
                        }
                    }
                    filteredExpenses = tempList;
                }
                FilterResults results = new FilterResults();
                results.values = filteredExpenses;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredExpenses = (List<Expense>) filterResults.values;
                notifyDataSetChanged();
                expenseAmountListener.totalExpenseAmount(filteredExpenses);
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView expenseAmountTV;
        TextView expenseNameTV;
        TextView expenseDateTimeTV;
        public RelativeLayout background;
        public CardView foreground;

        public ViewHolder(View itemView) {
            super(itemView);
            expenseAmountTV = itemView.findViewById(R.id.dailyExpenseAmount);
            expenseNameTV = itemView.findViewById(R.id.dailyExpenseType);
            expenseDateTimeTV = itemView.findViewById(R.id.dailyExpenseDateTime);
            background = itemView.findViewById(R.id.expenseRowBackground);
            foreground = itemView.findViewById(R.id.expenseRowForeground);
        }
    }
    public List<Expense>getExpenses(){
        return expenses;
    }
}
