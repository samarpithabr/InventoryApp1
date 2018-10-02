package com.example.shara.inventoryapp1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shara.inventoryapp1.data.EmployeeContract;
import com.example.shara.inventoryapp1.data.EmployeeContract.EmployeeEntry;

public class EmployeeCursorAdapter extends CursorAdapter {

    public EmployeeCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        Button slebtn = (Button) view.findViewById(R.id.sale);
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView qunatityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView suppliernameView = (TextView) view.findViewById(R.id.suppliername);
        TextView suppliercontactView = (TextView) view.findViewById(R.id.suppliercontact);
        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLOMN_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_QUANTITY);
        int supnameColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_SUPPLIER_NAME);
        int supcontactColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_SUPPLIER_CONTACT);
        String productName = cursor.getString(nameColumnIndex);
        int productPrice =cursor.getInt(priceColumnIndex);
        int productQuantity =cursor.getInt(quantityColumnIndex);
        String supplierName =cursor.getString(supnameColumnIndex);
        String suplierContact =cursor.getString(supcontactColumnIndex);
        nameTextView.setText(productName);
        priceTextView.setText(Integer.toString(productPrice));
        qunatityTextView.setText(Integer.toString(productQuantity));
        suppliernameView.setText(supplierName);
        suppliercontactView.setText(suplierContact);

        String quantityString = cursor.getString(quantityColumnIndex);
        final int currentQuantity = Integer.valueOf(quantityString);
        final int itemId = cursor.getInt(cursor.getColumnIndex(EmployeeEntry._ID));


        // Setting up "sale" functionality, where the quantity is decreased by 1
        // and updated using CONTENT_URI and ContentValues.
        slebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuantity > 1) {
                    int newQuantity = currentQuantity - 1;

                    Uri quantityUri = ContentUris.withAppendedId(EmployeeEntry.CONTENT_URI, itemId);

                    ContentValues values = new ContentValues();
                    values.put(EmployeeEntry.COLUMN_QUANTITY, newQuantity);
                    context.getContentResolver().update(quantityUri, values, null,
                            null);

                } else {
                    Toast.makeText(context, "quantity cannot be zero",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
}
    }


