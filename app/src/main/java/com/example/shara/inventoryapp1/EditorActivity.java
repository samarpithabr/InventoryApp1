package com.example.shara.inventoryapp1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shara.inventoryapp1.data.EmployeeContract.EmployeeEntry;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText mEditProductName;
    private EditText mEditPrice;
    private EditText mEditQuantity;
    private EditText mEditSupplierName;
    private EditText mEditSupplierContact;
    private Uri mCurrentProUri;
    private boolean mProHasChanged = false;
    private int currentQuantity;
    private static final int EXISTING_EMP_LOADER = 0;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        mCurrentProUri = intent.getData();
        if (mCurrentProUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_product));
            invalidateOptionsMenu();
        } else {

            setTitle(getString(R.string.editor_activity_title_edit_product));
            getLoaderManager().initLoader(EXISTING_EMP_LOADER, null, this);
        }

        mEditProductName = (EditText) findViewById(R.id.edit_product_name);
        mEditPrice = (EditText) findViewById(R.id.edit_price);
        mEditQuantity = (EditText) findViewById(R.id.edit_quantity_name);
        mEditSupplierName = (EditText) findViewById(R.id.edit_supplier_name);
        mEditSupplierContact = (EditText) findViewById(R.id.edit_supplier_contact);
        mEditProductName.setOnTouchListener(mTouchListener);
        mEditPrice.setOnTouchListener(mTouchListener);
        mEditQuantity.setOnTouchListener(mTouchListener);
        mEditSupplierName.setOnTouchListener(mTouchListener);
        mEditSupplierContact.setOnTouchListener(mTouchListener);
        Button incbtn = (Button) findViewById(R.id.increase_button);
        Button decbtn = (Button) findViewById(R.id.decrease_button);
        incbtn.setOnTouchListener(mTouchListener);
        decbtn.setOnTouchListener(mTouchListener);
        Button delete = (Button) findViewById(R.id.delete_pro);


        // Setting an OnClickListener to initiate the delettion of an item once clicked
        delete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePro();
            }
        });

        // Increase quantity by 1
        incbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String quantity = mEditQuantity.getText().toString();
                if (quantity == null || TextUtils.isEmpty(quantity)) {
                    Toast.makeText(EditorActivity.this, R.string.editor_enter_quantity,
                            Toast.LENGTH_SHORT).show();
                } else {
                    currentQuantity = Integer.parseInt(quantity);
                    mEditQuantity.setText(String.valueOf(currentQuantity + 1));
                }
            }
        });

        // Decrease quantity by 1 and making sure quantity is not empty and
        // does not equal to "0" before proceeding with decreasing it.
        decbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = mEditQuantity.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    Toast.makeText(EditorActivity.this, R.string.editor_quantity_empty,
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    currentQuantity = Integer.parseInt(quantity);
                    if (currentQuantity == 0) {
                        Toast.makeText(EditorActivity.this,
                                R.string.editor_quantity_zero, Toast.LENGTH_SHORT).show();
                    } else {
                        mEditQuantity.setText(String.valueOf(currentQuantity - 1));
                    }
                }
            }
        });
        final Button contactSupplierButton = findViewById(R.id.contact_supplier);

        contactSupplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mEditSupplierContact.getText().toString().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("Tel:" + phoneNumber));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    private void savePro() {
        String nameString = mEditProductName.getText().toString().trim();
        String priceString = mEditPrice.getText().toString().trim();
        String quantityString = mEditQuantity.getText().toString().trim();
        String supNameString = mEditSupplierName.getText().toString().trim();
        String supContString = mEditSupplierContact.getText().toString().trim();

        if (mCurrentProUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(supNameString) && TextUtils.isEmpty(supContString)) {

            return;
        }

        ContentValues values = new ContentValues();
        values.put(EmployeeEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(EmployeeEntry.COLOMN_PRICE, priceString);
        values.put(EmployeeEntry.COLUMN_QUANTITY, quantityString);
        values.put(EmployeeEntry.COLUMN_SUPPLIER_NAME, supNameString);
        values.put(EmployeeEntry.COLUMN_SUPPLIER_CONTACT, supContString);
        int quantity = 1;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(EmployeeEntry.COLUMN_QUANTITY, quantity);
        if (mCurrentProUri == null) {
            Uri newUri = getContentResolver().insert(EmployeeEntry.CONTENT_URI, values);
            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_EMP_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_Emp_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentProUri, values, null, null);
            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_EMP_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_EMP_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentProUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                savePro();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!mProHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {

        if (!mProHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {EmployeeEntry._ID,
                EmployeeEntry.COLUMN_PRODUCT_NAME,
                EmployeeEntry.COLOMN_PRICE,
                EmployeeEntry.COLUMN_QUANTITY,
                EmployeeEntry.COLUMN_SUPPLIER_NAME,
                EmployeeEntry.COLUMN_SUPPLIER_CONTACT};


        return new CursorLoader(this,   // Parent activity context
                mCurrentProUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort orderorder


    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {

            int nameColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLOMN_PRICE);
            int qunatityColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_QUANTITY);
            int supnameColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_SUPPLIER_NAME);
            int supcontactColumIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_SUPPLIER_CONTACT);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(qunatityColumnIndex);
            String supName = cursor.getString(supnameColumnIndex);
            String supContact = cursor.getString(supcontactColumIndex);

            // Update the views on the screen with the values from the database
            mEditProductName.setText(name);
            mEditPrice.setText(Integer.toString(price));
            mEditQuantity.setText(Integer.toString(quantity));
            mEditSupplierName.setText(supName);
            mEditSupplierContact.setText(supContact);

        }
    }


    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mEditProductName.setText("");
        mEditPrice.setText("");
        mEditQuantity.setText("");
        mEditSupplierName.setText("");
        mEditSupplierContact.setText("");

    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                deletePro();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePro() {

        if (mCurrentProUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentProUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}