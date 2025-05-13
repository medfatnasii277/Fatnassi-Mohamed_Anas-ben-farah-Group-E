package com.example.productprojectandroid.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.productprojectandroid.database.ProductDatabaseHelper
import com.example.productprojectandroid.databinding.ActivityAddProductBinding
import com.example.productprojectandroid.models.Product

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var dbHelper: ProductDatabaseHelper

    // Sample categories for dropdown
    private val categories = arrayOf(
        "Electronics", "Clothing", "Food", "Books", "Home", "Beauty", "Toys", "Sports", "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database helper
        dbHelper = ProductDatabaseHelper(this)

        // Setup spinner (dropdown menu)
        setupCategorySpinner()

        // Setup button click listeners
        binding.btnSaveProduct.setOnClickListener {
            saveProduct()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        binding.spinnerCategory.adapter = adapter
    }

    private fun saveProduct() {
        try {
            // Validate inputs
            val name = binding.etProductName.text.toString().trim()
            val description = binding.etProductDescription.text.toString().trim()
            val priceStr = binding.etProductPrice.text.toString().trim()
            val quantityStr = binding.etProductQuantity.text.toString().trim()
            val category = binding.spinnerCategory.selectedItem.toString()
            val isAvailable = binding.switchAvailable.isChecked
            val imageUrl = binding.etImageUrl.text.toString().trim()

            // Check required fields
            if (name.isEmpty()) {
                binding.etProductName.error = "Product name is required"
                return
            }

            if (priceStr.isEmpty()) {
                binding.etProductPrice.error = "Price is required"
                return
            }

            if (quantityStr.isEmpty()) {
                binding.etProductQuantity.error = "Quantity is required"
                return
            }

            // Parse numeric values
            val price = priceStr.toDouble()
            val quantity = quantityStr.toInt()

            // Create product object
            val product = Product(
                name = name,
                description = description,
                price = price,
                quantity = quantity,
                category = category,
                isAvailable = isAvailable,
                imageUrl = imageUrl
            )

            // Save to database
            val result = dbHelper.insertProduct(product)

            if (result > 0) {
                Toast.makeText(this, "Product saved successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save product", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}