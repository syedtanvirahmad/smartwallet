package com.pencilbox.user.smartwallet.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by User on 11/30/2017.
 */
@Entity(tableName = "income_table")
public class Income {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "income_id")
    private int incomeID;
    @ColumnInfo(name = "income_amount")
    private double incomeAmount;
    @ColumnInfo(name = "income_sourceId")
    private int incomeSourceId;
    @Ignore
    private String organization_name;
    @ColumnInfo(name = "income_date")
    private String incomeDate;
    @ColumnInfo(name = "source_description")
    private String sourceDescription;

    @Ignore
    public Income(double incomeAmount, int incomeSourceId, String incomeDate) {
        this.incomeAmount = incomeAmount;
        this.incomeSourceId = incomeSourceId;
        this.incomeDate = incomeDate;
    }

    public Income(double incomeAmount, int incomeSourceId, String incomeDate, String sourceDescription) {
        this.incomeAmount = incomeAmount;
        this.incomeSourceId = incomeSourceId;
        this.incomeDate = incomeDate;
        this.sourceDescription = sourceDescription;
    }



    @Ignore
    public Income(int incomeID, double incomeAmount, int incomeSourceId, String incomeDate) {
        this.incomeID = incomeID;
        this.incomeAmount = incomeAmount;
        this.incomeSourceId = incomeSourceId;
        this.incomeDate = incomeDate;
    }
    @Ignore
    public Income(int incomeID, double incomeAmount, int incomeSourceId, String incomeDate, String sourceDescription) {
        this.incomeID = incomeID;
        this.incomeAmount = incomeAmount;
        this.incomeSourceId = incomeSourceId;
        this.incomeDate = incomeDate;
        this.sourceDescription = sourceDescription;
    }

    public int getIncomeID() {
        return incomeID;
    }

    public void setIncomeID(int incomeID) {
        this.incomeID = incomeID;
    }

    public double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public int getIncomeSourceId() {
        return incomeSourceId;
    }

    public void setIncomeSourceId(int incomeSourceId) {
        this.incomeSourceId = incomeSourceId;
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public void setIncomeDate(String incomeDate) {
        this.incomeDate = incomeDate;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public void setSourceDescription(String sourceDescription) {
        this.sourceDescription = sourceDescription;
    }
}
