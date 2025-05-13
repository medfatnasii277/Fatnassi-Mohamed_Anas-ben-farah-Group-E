package com.example.productprojectandroid.database

import android.provider.BaseColumns

object ProductContract {

    object ProductEntry : BaseColumns {
        const val TABLE_NAME = "products"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PRICE = "price"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_IS_AVAILABLE = "is_available"
        const val COLUMN_IMAGE_URL = "image_url"

        const val SQL_CREATE_TABLE = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_QUANTITY INTEGER NOT NULL,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_IS_AVAILABLE INTEGER DEFAULT 1,
                $COLUMN_IMAGE_URL TEXT
            )
        """

        const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}