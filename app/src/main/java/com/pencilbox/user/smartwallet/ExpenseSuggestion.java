package com.pencilbox.user.smartwallet;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by User on 4/5/2018.
 */

public class ExpenseSuggestion extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.pencilbox.user.smartwallet.ExpenseSuggestion";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public ExpenseSuggestion() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
