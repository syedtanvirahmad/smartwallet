package com.pencilbox.user.smartwallet;

import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pencilbox.user.smartwallet.Database.PerDayExpenses;
import com.pencilbox.user.smartwallet.Interface.ExpenseMonthChangeListener;
import com.pencilbox.user.smartwallet.ViewModel.ReportViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
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
    private ExpenseMonthChangeListener expenseMonthChangeListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setCurrentDate(Calendar.getInstance());
        dateTV = findViewById(R.id.dateTextView);
        dateLayout = findViewById(R.id.dateLayout);
        appBarLayout = findViewById(R.id.appBarlayout);
        appBarLayout.setExpanded(false);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                Calendar c = date.getCalendar();
                int month = c.get(Calendar.MONTH);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
                String selectedMonth = sdf.format(c.getTime());
                expenseMonthChangeListener.onMonthChange(selectedMonth);
                //Toast.makeText(ViewReportActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
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
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    GraphReportFragment graphReportFragment = new GraphReportFragment();
                    expenseMonthChangeListener = graphReportFragment;
                    //expenseMonthChangeListener.onMonthChange(null);
                    return graphReportFragment;
                case 1:
                    return new SummeryReportFragment();
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
