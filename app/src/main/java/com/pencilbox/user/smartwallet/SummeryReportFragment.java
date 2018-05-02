package com.pencilbox.user.smartwallet;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pencilbox.user.smartwallet.Database.ExpensePerExpenseName;
import com.pencilbox.user.smartwallet.ViewModel.ReportViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummeryReportFragment extends Fragment {
    private ReportViewModel reportViewModel;
    private static final String TAG = SummeryReportFragment.class.getSimpleName();
    private TextView showExpenseSummeryTV;

    public SummeryReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_summery_report, container, false);
        showExpenseSummeryTV = v.findViewById(R.id.showExpensePerNameTV);
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        List<ExpensePerExpenseName>lists = reportViewModel.getExpensesForExpenseNames();
        StringBuilder builder = new StringBuilder();
        for(ExpensePerExpenseName ep : lists){
            String name = ep.getExpense_name();
            String toUpperFistString = name.substring(0,1).toUpperCase()+name.substring(1);
            builder.append(toUpperFistString).append(" : ").append(ep.getExpense_amount()).append("\n");
        }
        showExpenseSummeryTV.setText(builder.toString());
        return v;
    }

}
