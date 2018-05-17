package com.pencilbox.user.smartwallet.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pencilbox.user.smartwallet.Utils.Constants;

/**
 * Created by User on 11/21/2017.
 */
@Database(entities = {Expense.class,IncomeSource.class,Income.class,BankAccount.class},version = 6)
public abstract class ExpenseDatabase extends RoomDatabase{
    public abstract ExpenseDao expenseDao();

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `income_source`(`id` INTEGER PRIMARY KEY NOT NULL, " +
                    "`organization_name` TEXT, `income_sourceId` INTEGER NOT NULL, " +
                    "`income_amount` REAL NOT NULL)");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `income_table`(`id` INTEGER PRIMARY KEY NOT NULL, `income_date` TEXT, `income_sourceId` INTEGER NOT NULL, " +
                    "`income_amount` REAL NOT NULL)");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'income_source_new'('id' INTEGER PRIMARY KEY NOT NULL, " +
                    "'organization_name' TEXT, 'income_type' TEXT)");
            database.execSQL("INSERT INTO income_source_new(id,organization_name) SELECT id,organization_name FROM income_source");
            database.execSQL("DROP TABLE 'income_source'");
            database.execSQL("ALTER TABLE 'income_source_new' RENAME TO 'income_source'");
            database.execSQL("ALTER TABLE 'income_table' ADD COLUMN 'source_description' TEXT");
        }
    };
    static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE income_table_new(income_id INTEGER PRIMARY KEY NOT NULL, " +
                    "income_date TEXT, " +
                    "income_sourceId INTEGER NOT NULL, " +
                    "income_amount REAL NOT NULL, " +
                    "source_description TEXT)");
            database.execSQL("INSERT INTO income_table_new(income_id,income_date,income_sourceId,income_amount,source_description) " +
                    "SELECT id,income_date,income_sourceId,income_amount,source_description FROM income_table");
            database.execSQL("DROP TABLE income_table");
            database.execSQL("ALTER TABLE income_table_new RENAME TO income_table");
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `bank_account`(" +
                    "`accountId` INTEGER PRIMARY KEY NOT NULL, " +
                    "`bank_name` TEXT, " +
                    "`account_name` TEXT, " +
                    "`account_no` TEXT, " +
                    "`total_balance` REAL NOT NULL)");
        }
    };
    public static ExpenseDatabase getInstance(Context context){
        return Room.databaseBuilder(context,ExpenseDatabase.class, Constants.DATABASE_NAME)
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4,MIGRATION_4_5,MIGRATION_5_6)
                .build();
    }
}
