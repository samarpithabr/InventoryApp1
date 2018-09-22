package com.example.shara.inventoryapp1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.shara.inventoryapp1.data.EmployeeContract.EmployeeEntry;
public class EmployeedbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = EmployeedbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "pets.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;


    public EmployeedbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_STORE_TABLE = "CREATE TABLE " +EmployeeEntry.TABLE_NAME + " ("
                + EmployeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +EmployeeEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                +EmployeeEntry.COLOMN_PRICE+ " INTEGER NOT NULL, "
                +EmployeeEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 1, "
                + EmployeeEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL,"
                + EmployeeEntry.COLUMN_SUPPLIER_CONTACT + "TEXT NOT NULL );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_STORE_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}



