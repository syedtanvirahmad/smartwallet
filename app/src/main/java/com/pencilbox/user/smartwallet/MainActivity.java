package com.pencilbox.user.smartwallet;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobTrigger;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.pencilbox.user.smartwallet.Adapter.ExpenseAdapter;
import com.pencilbox.user.smartwallet.Database.Expense;
import com.pencilbox.user.smartwallet.Database.ExpenseDatabase;
import com.pencilbox.user.smartwallet.Interface.LoadFilteredExpenses;
import com.pencilbox.user.smartwallet.Interface.MainView;
import com.pencilbox.user.smartwallet.Interface.MainViewImpl;
import com.pencilbox.user.smartwallet.Interface.OnAddExpenseFinishedListener;
import com.pencilbox.user.smartwallet.Utils.AddExpenseDialog;
import com.pencilbox.user.smartwallet.Utils.Constants;
import com.pencilbox.user.smartwallet.Utils.ExpenseFilter;
import com.pencilbox.user.smartwallet.ViewModel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView,MainView.ExpenseAmountListener{
    private FirebaseJobDispatcher jobDispatcher;
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView expenseRV;
    private ExpenseAdapter expenseAdapter;
    private ExpenseDatabase expenseDatabase;
    private TextView expenseAmountTV, labelTV;
    List<Expense>expenses = new ArrayList<>();
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private MainViewModel mainViewModel;
    private LinearLayout bottomSheetRoot;
    private BottomSheetBehavior bottomSheetBehavior;
    private CoordinatorLayout root;
    private TextInputEditText amountET, expenseNameET, expenseDateET;
    private SearchView expenseNameSV;
    private ExpenseDateListener expenseDateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        root = findViewById(R.id.rootLayout);
        bottomSheetRoot = findViewById(R.id.bottomSheetRoot);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetRoot);
        amountET = bottomSheetRoot.findViewById(R.id.expenseAmountET);
        expenseNameET = bottomSheetRoot.findViewById(R.id.expenseNameET);
        expenseDateET = bottomSheetRoot.findViewById(R.id.expenseDateTimeET);
        expenseNameSV = bottomSheetRoot.findViewById(R.id.expenseNameSV);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        expenseNameSV.setSearchableInfo(searchableInfo);

        labelTV = bottomSheetRoot.findViewById(R.id.addExpenseLabel);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //mainViewModel.uploadAllExpenses();
        expenseRV = findViewById(R.id.dailyExpenseRV);
        toolbar = findViewById(R.id.toolbar);
        expenseAmountTV = findViewById(R.id.totalExpenseForTodayTV);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("Expense List");
        setSupportActionBar(toolbar);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(expenseRV);
        expenseAdapter = new ExpenseAdapter(this,expenses);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        expenseRV.setLayoutManager(llm);
        expenseRV.setItemAnimator(new DefaultItemAnimator());
        expenseRV.setAdapter(expenseAdapter);

        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }
        expenseNameSV.setOnSuggestionListener(suggestionListener);
        bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
        labelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBottomSheet();
            }
        });
        mainViewModel.getCurrentDayExpense().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                expenseAdapter.addExpenseItems(expenses);
                expenseAmountTV.setText("Total: "+String.valueOf(mainViewModel.getTotalExpenseAmount(expenses)));
            }
        });
        expenseDateListener = new MainViewImpl(this);
        expenseDateET.setText(getCurrentDate());
        expenseDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseDateListener.onAddExpenseDate(MainActivity.this);
            }
        });

        Calendar calendar = Calendar.getInstance();
        Date current = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR,1);
        Date tomorrow = calendar.getTime();

        Log.e(TAG, "today: "+current);
        Log.e(TAG, "tomorrow: "+tomorrow);

        /*Job myjob = jobDispatcher.newJobBuilder()
                .setService(DailyReportService.class)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(10,60 * 60))
                .setTag("test")
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        jobDispatcher.mustSchedule(myjob);*/
        //jobDispatcher.cancel("test"); // cancel job if needed
    }

    private void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            expenseDateET.setText(getCurrentDate());
            //bottomSheetBehavior.setText("Close sheet");
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //bottomSheetBehavior.setText("Expand sheet");
        }
    }

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
            new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.searchItem).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.searchItem));
        searchView.setOnQueryTextListener(queryListener);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = searchView.getSuggestionsAdapter().getCursor();
                if(cursor.moveToPosition(position)){
                    String item = cursor.getString(2);
                    //Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
                    searchView.setQuery(item,false);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                startActivity(new Intent(MainActivity.this,DashboardActivity.class));
                break;
            case R.id.viewReport:
                startActivity(new Intent(MainActivity.this,ViewReportActivity.class));
                break;
            case R.id.searchItem:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getReport(View view) {
        switch (view.getId()){
            case R.id.totalExpenseTodayBtn:
                mainViewModel.getCurrentDayExpense().observe(this, new Observer<List<Expense>>() {
                    @Override
                    public void onChanged(@Nullable List<Expense> expenses) {
                        expenseAdapter.addExpenseItems(expenses);
                        expenseAmountTV.setText("Total: "+String.valueOf(mainViewModel.getTotalExpenseAmount(expenses)));
                    }
                });
                break;
            case R.id.totalExpenseThisMonthBtn:
                mainViewModel.getCurrentMonthExpense().observe(this, new Observer<List<Expense>>() {
                    @Override
                    public void onChanged(@Nullable List<Expense> expenses) {
                        expenseAdapter.addExpenseItems(expenses);
                        expenseAmountTV.setText("Total: "+String.valueOf(mainViewModel.getTotalExpenseAmount(expenses)));
                    }
                });
                break;
            case R.id.totalExpenseAllBtnBtn:
                mainViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
                    @Override
                    public void onChanged(@Nullable List<Expense> expenses) {
                        expenseAdapter.addExpenseItems(expenses);
                        expenseAmountTV.setText("Total: "+String.valueOf(mainViewModel.getTotalExpenseAmount(expenses)));
                    }
                });
                break;
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            Snackbar snackbar = Snackbar.make(root,"delete?",Snackbar.LENGTH_LONG);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Expense>loadedExpenses = expenseAdapter.getExpenses();
                    Expense expense = loadedExpenses.get(position);
                    mainViewModel.deleteSpecificExpense(expense);
                    mainViewModel.deleteSpecificExpenseFromCloud(expense);
                    loadedExpenses.remove(position);
                    expenseAdapter.addExpenseItems(loadedExpenses);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);

            snackbar.show();
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            try {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();

                    float width = height / 5;
                    //Log.e(TAG, "item height: "+height);
                    //Log.e(TAG, "item width: "+width);
                    viewHolder.itemView.setTranslationX(dX / 5);
                    Paint paint = new Paint();
                    paint.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() + dX / 5, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, paint);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_delete);
                    RectF icon_dest = new RectF((float) (itemView.getRight() + dX /7), (float) itemView.getTop()+width, (float) itemView.getRight()+dX/20, (float) itemView.getBottom()-width);
                    c.drawBitmap(icon,  null, icon_dest, paint);
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void addNewExpense(View view) {
        String amountString = amountET.getText().toString();
        //String name = expenseNameET.getText().toString();
        String name1 = expenseNameSV.getQuery().toString().toLowerCase();
        String dateTime = expenseDateET.getText().toString();
        if(validateInputField(amountString,name1,dateTime)){
            SearchRecentSuggestions suggestions =
                    new SearchRecentSuggestions(this, ExpenseSuggestion.AUTHORITY,
                            ExpenseSuggestion.MODE);
            suggestions.saveRecentQuery(name1,null);
            Expense expense = new Expense(name1,Double.parseDouble(amountString),dateTime);
            //add expense to firebase
            //mainViewModel.insertNewExpenseToCloud(expense);
            boolean status
                    = mainViewModel.insertNewExpense(expense);
            if(status){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                //expenseNameET.setText("");
                expenseNameSV.setQuery("",false);
                amountET.setText("");
                expenseDateET.setText(getCurrentDate());

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mainViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
                    @Override
                    public void onChanged(@Nullable List<Expense> expenses) {
                        expenseAdapter.addExpenseItems(expenses);
                        expenseAmountTV.setText("Total: "+String.valueOf(mainViewModel.getTotalExpenseAmount(expenses)));
                    }
                });
            }else{
                Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Please fill all the input fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputField(String amountString, String name1, String dateTime) {

        if(amountString.isEmpty() || name1.isEmpty() || dateTime.isEmpty()){
            return false;
        }
        return true;
    }

    private static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }

    private SearchView.OnSuggestionListener suggestionListener = new SearchView.OnSuggestionListener() {
        @Override
        public boolean onSuggestionSelect(int position) {
            return false;
        }

        @Override
        public boolean onSuggestionClick(int position) {

            //expenseNameSV.setQuery();
            /*CursorAdapter adapter = expenseNameSV.getSuggestionsAdapter();
            String item = (String) adapter.getItem(position);*/
            Cursor cursor = expenseNameSV.getSuggestionsAdapter().getCursor();
            if(cursor.moveToPosition(position)){
                String item = cursor.getString(2);
                //Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
                expenseNameSV.setQuery(item,false);
            }

            return true;
        }
    };

    @Override
    public void onSetDateListener(String date) {
        expenseDateET.setText(date);
    }

    private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            expenseAdapter.getFilter().filter(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            expenseAdapter.getFilter().filter(newText);
            return false;
        }
    };

    @Override
    public void totalExpenseAmount(List<Expense> expenses) {
        expenseAmountTV.setText("Total: "+String.valueOf(mainViewModel.getTotalExpenseAmount(expenses)));
    }
}
