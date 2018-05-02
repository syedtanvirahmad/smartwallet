package com.pencilbox.user.smartwallet.Database;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by User on 12/3/2017.
 */

public class IncomeWithSource {
    @Embedded
    public IncomeSource incomeSource;
    @Relation(parentColumn = "id", entityColumn = "income_sourceId",entity = Income.class)
    public List<FullIncome>incomes;
}
