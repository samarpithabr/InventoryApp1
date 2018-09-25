package com.example.shara.inventoryapp1;

        import android.content.ContentValues;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v4.app.NavUtils;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.EditText;
        import android.widget.Toast;
        import com.example.shara.inventoryapp1.data.EmployeeContract.EmployeeEntry;
        import com.example.shara.inventoryapp1.data.EmployeedbHelper;

public class EditorActivity extends AppCompatActivity {
    private EditText mEditProductName;
    private EditText mEditPrice;
    private EditText mEditQuantity;
    private EditText mSupplierName;
    private EditText mEditSupplierContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mEditProductName = (EditText) findViewById(R.id.edit_product_name);
        mEditPrice = (EditText) findViewById(R.id.edit_price);
        mEditQuantity = (EditText) findViewById(R.id.edit_quantity_name);
        mSupplierName = (EditText) findViewById(R.id.edit_supplier_name);
        mEditSupplierContact = (EditText) findViewById(R.id.edit_supplier_contact);

    }
    private void insertPet() {
        String nameString = mEditProductName.getText().toString().trim();
        String price = mEditPrice.getText().toString().trim();
        final int finalPrice = Integer.parseInt(price);
        String quantity = mEditQuantity.getText().toString().trim();
        final int finalquantity = Integer.parseInt(quantity);
        String suppliername = mSupplierName.getText().toString().trim();
        String suppliercontact = mEditSupplierContact.getText().toString().trim();
        EmployeedbHelper mDbHelper = new EmployeedbHelper((this));
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EmployeeEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(EmployeeEntry.COLOMN_PRICE, finalPrice);
        values.put(EmployeeEntry.COLUMN_QUANTITY, finalquantity);
        values.put(EmployeeEntry.COLUMN_SUPPLIER_NAME, suppliername);
        values.put(EmployeeEntry.COLUMN_SUPPLIER_CONTACT, suppliercontact);
        long newRowId = db.insert(EmployeeEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "well done:" + newRowId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                insertPet();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
