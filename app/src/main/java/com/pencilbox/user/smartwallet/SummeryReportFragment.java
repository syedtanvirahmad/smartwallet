package com.pencilbox.user.smartwallet;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pencilbox.user.smartwallet.Adapter.ExpenseSummeryAdapter;
import com.pencilbox.user.smartwallet.Database.ExpensePerExpenseName;
import com.pencilbox.user.smartwallet.Interface.ExpenseReport;
import com.pencilbox.user.smartwallet.ViewModel.ReportViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummeryReportFragment extends Fragment implements ExpenseReport.SummeryReport{
    private ReportViewModel reportViewModel;
    private static final String TAG = SummeryReportFragment.class.getSimpleName();
    private TextView showExpenseSummeryTV;
    private RecyclerView recyclerView;
    private ExpenseSummeryAdapter adapter;

    public SummeryReportFragment() {
        // Required empty public constructor
    }

    public static SummeryReportFragment getInstance(){
        SummeryReportFragment fragment = new SummeryReportFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_summery_report, container, false);
        recyclerView = v.findViewById(R.id.rv_expenseNamesWithTotalAmount);
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        getSummeryData(null);
        return v;
    }

    @Override
    public void getSummeryData(String month) {
        //get total expense for this month
        //average expense per day
        //projected expensne ??

        List<ExpensePerExpenseName>lists = reportViewModel.getExpensesForExpenseNames(month);
        adapter = new ExpenseSummeryAdapter(lists);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }
}
