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
import com.example.shara.inventoryapp1.data.EmployeeContract;


public class MainActivity extends AppCompatActivity {
    private EmployeedbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
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

        Cursor cursor = db.rawQuery("SELECT * FROM " + EmployeeContract.EmployeeEntry.TABLE_NAME, null);
        try {

            TextView displayView = findViewById(R.id.text_view_pet);
            displayView.setText("Number of rows in store database table: " + cursor.getCount());
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
    private void insertpet(){
        SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(EmployeeEntry.COLUMN_PRODUCT_NAME,"Pen");
        values.put(EmployeeEntry.COLOMN_PRICE,34);
        values.put(EmployeeEntry.COLUMN_QUANTITY,3);
        values.put(EmployeeEntry.COLUMN_SUPPLIER_NAME,"suchi");
        values.put(EmployeeEntry.COLUMN_SUPPLIER_CONTACT,"2356276298");

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
