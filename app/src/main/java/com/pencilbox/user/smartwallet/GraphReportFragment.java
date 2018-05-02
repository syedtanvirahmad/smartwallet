package com.pencilbox.user.smartwallet;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.pencilbox.user.smartwallet.Interface.ExpenseReport;
import com.pencilbox.user.smartwallet.ViewModel.ReportViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphReportFragment extends Fragment implements ExpenseReport {
    private BarChart mBarChart;
    private ReportViewModel reportViewModel;

    public GraphReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_graph_report, container, false);
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        mBarChart = v.findViewById(R.id.barChart);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setMaxVisibleValueCount(31);
        mBarChart.setDrawGridBackground(true);
        mBarChart.setData(reportViewModel.getBarDataForSelectedMonth(null));
        return v;
    }

    @Override
    public void getBarChartData(String month) {
        //Toast.makeText(getActivity(), "called : "+month, Toast.LENGTH_SHORT).show();
        //mBarChart.clearValues();
        mBarChart.clear();
        mBarChart.setData(reportViewModel.getBarDataForSelectedMonth(month));
    }
}
