package com.pencilbox.user.smartwallet;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.pencilbox.user.smartwallet.Database.Expense;
import com.pencilbox.user.smartwallet.Interface.ExpenseReport;
import com.pencilbox.user.smartwallet.ViewModel.ReportViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewReportActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private static final String TAG = ViewReportActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ReportPagerAdapter pagerAdapter;
    private AppBarLayout appBarLayout;
    private boolean isAppbarExpanded = false;
    private TextView dateTV;
    private RelativeLayout dateLayout;
    //private CalendarView calendarView;
    private MaterialCalendarView calendarView;
    private ExpenseReport expenseReport;
    private ExpenseReport.SummeryReport summeryReport;
    private SimpleDateFormat sdf;
    private Fragment[]fragments = new Fragment[2];
    private ReportViewModel reportViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        calendarView = findViewById(R.id.calendarView);
        calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        sdf = new SimpleDateFormat("MMMM yyyy");
        calendarView.setDateSelected(CalendarDay.today(),true);
        dateTV = findViewById(R.id.dateTextView);
        dateLayout = findViewById(R.id.dateLayout);
        appBarLayout = findViewById(R.id.appBarlayout);
        appBarLayout.setExpanded(false);
        dateTV.setText(sdf.format(new Date()));
        fragments[0] = new GraphReportFragment();
        fragments[1] = SummeryReportFragment2.getInstance(CalendarDay.today());
        //expenseReport = new GraphReportFragment();
        //summeryReport = new SummeryReportFragment();

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                dateTV.setText(reportViewModel.getSelectedMonthFormattedText(date));
                try{
                    expenseReport.getBarChartData(reportViewModel.getSelectedMonth(date));
                    summeryReport.getSummeryData(reportViewModel.getSelectedMonth(date),date);
                }catch (Exception e){

                }
            }
        });

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView dropDownImage = findViewById(R.id.dropDownImageView);
                float rotation = isAppbarExpanded ? 0 : 180;
                ViewCompat.animate(dropDownImage).rotation(rotation).start();

                if(isAppbarExpanded){
                    appBarLayout.setExpanded(false);
                    isAppbarExpanded = false;

                }else{
                    appBarLayout.setExpanded(true);
                    isAppbarExpanded = true;
                }
            }
        });
        toolbar = findViewById(R.id.report_toolbar);
        setUpActionBar();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tabLayout = findViewById(R.id.reportTabLayout);
        viewPager = findViewById(R.id.reportViewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Graph"));
        tabLayout.addTab(tabLayout.newTab().setText("Summery"));
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
        pagerAdapter = new ReportPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.report_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public static class MyXAxisValueFormatter implements IAxisValueFormatter{
        private List<String>days;

        public MyXAxisValueFormatter(List<String> days) {
            this.days = days;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return days.get((int) value);
        }
    }

    private class ReportPagerAdapter extends FragmentPagerAdapter{
        private int tabCount;
        public ReportPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object fragment = super.instantiateItem(container,position);
            fragments[position] = (Fragment) fragment;
            if(fragments[position] instanceof ExpenseReport){
                expenseReport = (ExpenseReport) fragments[position];
            }else if(fragments[position] instanceof ExpenseReport.SummeryReport){
                summeryReport = (ExpenseReport.SummeryReport) fragments[position];
            }
            return fragment;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    //GraphReportFragment graphReportFragment = new GraphReportFragment();
                    expenseReport = (ExpenseReport) fragments[position];
                    return fragments[position];
                case 1:
                    //SummeryReportFragment summeryReportFragment = new SummeryReportFragment();
                    summeryReport = (ExpenseReport.SummeryReport) fragments[position];
                    return fragments[position];
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
    private void setUpActionBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }
}
