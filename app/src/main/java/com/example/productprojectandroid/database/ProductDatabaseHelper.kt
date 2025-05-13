package com.example.productprojectandroid.database


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.productprojectandroid.models.Product


class ProductDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "product_management.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ProductContract.ProductEntry.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(ProductContract.ProductEntry.SQL_DELETE_TABLE)
        onCreate(db)
    }

    // CRUD Operations

    // Create - Insert a new product
    fun insertProduct(product: Product): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ProductContract.ProductEntry.COLUMN_NAME, product.name)
            put(ProductContract.ProductEntry.COLUMN_DESCRIPTION, product.description)
            put(ProductContract.ProductEntry.COLUMN_PRICE, product.price)
            put(ProductContract.ProductEntry.COLUMN_QUANTITY, product.quantity)
            put(ProductContract.ProductEntry.COLUMN_CATEGORY, product.category)
            put(ProductContract.ProductEntry.COLUMN_IS_AVAILABLE, if (product.isAvailable) 1 else 0)
            put(ProductContract.ProductEntry.COLUMN_IMAGE_URL, product.imageUrl)
        }

        val id = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values)
        db.close()
        return id
    }

    // Read - Get all products
    fun getAllProducts(): ArrayList<Product> {
        val productList = ArrayList<Product>()
        val selectQuery = "SELECT * FROM ${ProductContract.ProductEntry.TABLE_NAME}"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_DESCRIPTION)),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRICE)),
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_QUANTITY)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_CATEGORY)),
                    isAvailable = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_IS_AVAILABLE)) == 1,
                    imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_IMAGE_URL))
                )
                productList.add(product)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return productList
    }

    // Read - Get a single product by ID
    fun getProduct(id: Long): Product? {
        val db = this.readableDatabase
        var product: Product? = null

        val cursor = db.query(
            ProductContract.ProductEntry.TABLE_NAME,
            null,
            "${ProductContract.ProductEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            product = Product(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_DESCRIPTION)),
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRICE)),
                quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_QUANTITY)),
                category = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_CATEGORY)),
                isAvailable = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_IS_AVAILABLE)) == 1,
                imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_IMAGE_URL))
            )
        }

        cursor.close()
        db.close()
        return product
    }

    // Update - Update an existing product
    fun updateProduct(product: Product): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ProductContract.ProductEntry.COLUMN_NAME, product.name)
            put(ProductContract.ProductEntry.COLUMN_DESCRIPTION, product.description)
            put(ProductContract.ProductEntry.COLUMN_PRICE, product.price)
            put(ProductContract.ProductEntry.COLUMN_QUANTITY, product.quantity)
            put(ProductContract.ProductEntry.COLUMN_CATEGORY, product.category)
            put(ProductContract.ProductEntry.COLUMN_IS_AVAILABLE, if (product.isAvailable) 1 else 0)
            put(ProductContract.ProductEntry.COLUMN_IMAGE_URL, product.imageUrl)
        }

        val rowsAffected = db.update(
            ProductContract.ProductEntry.TABLE_NAME,
            values,
            "${ProductContract.ProductEntry.COLUMN_ID} = ?",
            arrayOf(product.id.toString())
        )

        db.close()
        return rowsAffected
    }

    // Delete - Delete a product
    fun deleteProduct(id: Long): Int {
        val db = this.writableDatabase
        val rowsAffected = db.delete(
            ProductContract.ProductEntry.TABLE_NAME,
            "${ProductContract.ProductEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )

        db.close()
        return rowsAffected
    }
}



