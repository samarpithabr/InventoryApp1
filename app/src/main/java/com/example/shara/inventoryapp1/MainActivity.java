package com.example.shara.inventoryapp1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.shara.inventoryapp1.data.EmployeeContract.EmployeeEntry;
import com.example.shara.inventoryapp1.data.EmployeedbHelper;

public class MainActivity extends AppCompatActivity {
    private EmployeedbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new EmployeedbHelper(this);

        displayDatabaseInfo();
    }


    private void displayDatabaseInfo() {

        EmployeedbHelper mDbHelper = new EmployeedbHelper(this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {EmployeeEntry._ID,
                EmployeeEntry.COLUMN_PRODUCT_NAME,
                EmployeeEntry.COLOMN_PRICE, EmployeeEntry.COLUMN_QUANTITY, EmployeeEntry.COLUMN_SUPPLIER_NAME, EmployeeEntry.COLUMN_SUPPLIER_CONTACT};


        Cursor cursor = db.query(
                EmployeeEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order


        TextView displayView = findViewById(R.id.text_view_pet);

        try {
            displayView.setText("The Employee table contains " + cursor.getCount() + " Employee.\n\n");
            displayView.append(EmployeeEntry._ID + " - " +
                    EmployeeEntry.COLUMN_PRODUCT_NAME + " - " +
                    EmployeeEntry.COLOMN_PRICE + " - " +
                    EmployeeEntry.COLUMN_QUANTITY + " - " +
                    EmployeeEntry.COLUMN_SUPPLIER_NAME + " - " +
                    EmployeeEntry.COLUMN_SUPPLIER_CONTACT + "\n");
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(EmployeeEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLOMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_QUANTITY);
            int supnameColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_SUPPLIER_NAME);
            int suppliercontColumIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_SUPPLIER_CONTACT);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentprice = cursor.getInt(priceColumnIndex);
                int currentquantity = cursor.getInt(quantityColumnIndex);
                String currentsupname = cursor.getString(supnameColumnIndex);
                String currentsupcontact = cursor.getString(suppliercontColumIndex);

                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentprice + " - " +
                        currentquantity + " - " +
                        currentsupname + " - " +
                        currentsupcontact));
            }
        } finally {

            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void insertpet() {
        SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EmployeeEntry.COLUMN_PRODUCT_NAME, "Pen");
        values.put(EmployeeEntry.COLOMN_PRICE, 34);
        values.put(EmployeeEntry.COLUMN_QUANTITY, 3);
        values.put(EmployeeEntry.COLUMN_SUPPLIER_NAME, "suchi");
        values.put(EmployeeEntry.COLUMN_SUPPLIER_CONTACT, "2356276298");
        long newRowId = db.insert(EmployeeEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_insert_dummy_data:
                insertpet();
                displayDatabaseInfo();
                return true;

            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
