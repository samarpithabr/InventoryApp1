package com.example.shara.inventoryapp1.data;

import android.provider.BaseColumns;

public class EmployeeContract {

    public static final class EmployeeEntry implements BaseColumns {

        public static final String TABLE_NAME = "store";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "productname";
        public static final String COLOMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "suppliername";
        public static final String COLUMN_SUPPLIER_CONTACT = "suppliercontact";

    }
}




