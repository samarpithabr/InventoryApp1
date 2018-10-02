package com.example.shara.inventoryapp1;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.shara.inventoryapp1.data.EmployeeContract.EmployeeEntry;
import com.example.shara.inventoryapp1.data.EmployeedbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EmployeedbHelper mDbHelper;
    private static final int Employer_LOADER = 0;
    EmployeeCursorAdapter mCursorAdapter;


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
        ListView proListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        proListView.setEmptyView(emptyView);

        mCursorAdapter = new EmployeeCursorAdapter(this, null);
        proListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        proListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentProUri = ContentUris.withAppendedId(EmployeeEntry.CONTENT_URI, id);
                intent.setData(currentProUri);
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(Employer_LOADER, null, this);
    }

    private void insertPro() {
        ContentValues values = new ContentValues();
        values.put(EmployeeEntry.COLUMN_PRODUCT_NAME, "Toto");
        values.put(EmployeeEntry.COLOMN_PRICE, 5);
        values.put(EmployeeEntry.COLUMN_QUANTITY, 3);
        values.put(EmployeeEntry.COLUMN_SUPPLIER_NAME, "samarpitha");
        values.put(EmployeeEntry.COLUMN_SUPPLIER_CONTACT, "30281615271");
        Uri newUri = getContentResolver().insert(EmployeeEntry.CONTENT_URI, values);

    }

    private void deleteAllPro() {
        int rowsDeleted = getContentResolver().delete(EmployeeEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " Rows deleted from Employee database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPro();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPro();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {EmployeeEntry._ID,
                EmployeeEntry.COLUMN_PRODUCT_NAME,
                EmployeeEntry.COLOMN_PRICE,
                EmployeeEntry.COLUMN_QUANTITY,
                EmployeeEntry.COLUMN_SUPPLIER_NAME,
                EmployeeEntry.COLUMN_SUPPLIER_CONTACT};
        return new CursorLoader(this,   // Parent activity context
                EmployeeEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort orderorder

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
