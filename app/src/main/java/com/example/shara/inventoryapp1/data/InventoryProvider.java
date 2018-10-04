package com.example.shara.inventoryapp1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.shara.inventoryapp1.R;

import static android.widget.Toast.LENGTH_SHORT;

public class InventoryProvider extends ContentProvider {

    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();
    private static final int Employee = 100;
    private static final int Employee_Id = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY, EmployeeContract.PATH_EMPLOYEE, Employee);
        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY, EmployeeContract.PATH_EMPLOYEE + "/#", Employee_Id);
    }

    private EmployeedbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new EmployeedbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case Employee:
                cursor = database.query(EmployeeContract.EmployeeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case Employee_Id:
                selection = EmployeeContract.EmployeeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(EmployeeContract.EmployeeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Employee:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertPet(Uri uri, ContentValues values) {
        String name = values.getAsString(EmployeeContract.EmployeeEntry.COLUMN_PRODUCT_NAME);
        try {
            if ((name == null) || (TextUtils.isEmpty(name)))
                throw new IllegalArgumentException("PRODUCT requires a name");

        } catch (Exception e) {
            Toast.makeText(getContext(), "Please Enter proper Name" , LENGTH_SHORT).show();
        }

        Integer price = values.getAsInteger(EmployeeContract.EmployeeEntry.COLOMN_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Product requires valid Price");
        }
        Integer quantity = values.getAsInteger(EmployeeContract.EmployeeEntry.COLUMN_QUANTITY);
        if (quantity != null && quantity < 1) {
            throw new IllegalArgumentException("Product requires valid Quantity");
        }
        String supplierName = values.getAsString(EmployeeContract.EmployeeEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Product requires valid supplier name");
        }
        String supplierContact = values.getAsString(EmployeeContract.EmployeeEntry.COLUMN_SUPPLIER_CONTACT);
        if (supplierContact == null) {
            throw new IllegalArgumentException("Product requires valid supplier contact");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(EmployeeContract.EmployeeEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Employee:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case Employee_Id:
                selection = EmployeeContract.EmployeeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(EmployeeContract.EmployeeEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(EmployeeContract.EmployeeEntry.COLUMN_PRODUCT_NAME);
            if (TextUtils.isEmpty(name))
                throw new IllegalArgumentException("PRODUCT requires a Name cannot be empty");
        }
        if (values.containsKey(EmployeeContract.EmployeeEntry.COLOMN_PRICE)) {
            Integer price = values.getAsInteger(EmployeeContract.EmployeeEntry.COLOMN_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("product requires price");
            }
        }
        if (values.containsKey(EmployeeContract.EmployeeEntry.COLUMN_QUANTITY)) {

            Integer quantity = values.getAsInteger(EmployeeContract.EmployeeEntry.COLUMN_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("product requires quantity");
            }
        }
        if (values.containsKey(EmployeeContract.EmployeeEntry.COLUMN_SUPPLIER_NAME)) {
            String supname = values.getAsString(EmployeeContract.EmployeeEntry.COLUMN_SUPPLIER_NAME);
            if (supname == null) {
                throw new IllegalArgumentException("PRODUCT requires a supplier name");
            }
        }
        if (values.containsKey(EmployeeContract.EmployeeEntry.COLUMN_SUPPLIER_CONTACT)) {
            String supconta = values.getAsString(EmployeeContract.EmployeeEntry.COLUMN_SUPPLIER_CONTACT);
            if (supconta == null) {
                throw new IllegalArgumentException("PRODUCT requires a Supplier contact");
            }
        }
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(EmployeeContract.EmployeeEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Employee:
                rowsDeleted = database.delete(EmployeeContract.EmployeeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case Employee_Id:
                selection = EmployeeContract.EmployeeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(EmployeeContract.EmployeeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Employee:
                return EmployeeContract.EmployeeEntry.CONTENT_LIST_TYPE;
            case Employee_Id:
                return EmployeeContract.EmployeeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}