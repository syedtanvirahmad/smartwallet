package com.pencilbox.user.smartwallet.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by User on 11/29/2017.
 */
@Entity(tableName = "income_source")
public class IncomeSource{
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "organization_name")
    private String organization;
    @ColumnInfo(name = "income_type")
    private String incomeType;

    public IncomeSource(String organization, String incomeType) {
        this.organization = organization;
        this.incomeType = incomeType;
    }
    @Ignore
    public IncomeSource(int id, String organization, String incomeType) {
        this.id = id;
        this.organization = organization;
        this.incomeType = incomeType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

}
