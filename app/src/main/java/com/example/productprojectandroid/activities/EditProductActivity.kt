package com.example.productprojectandroid.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.productprojectandroid.database.ProductDatabaseHelper
import com.example.productprojectandroid.databinding.ActivityEditProductBinding
import com.example.productprojectandroid.models.Product


class EditProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProductBinding
    private lateinit var dbHelper: ProductDatabaseHelper
    private var productId: Long = -1
    private var product: Product? = null

    // Sample categories for dropdown
    private val categories = arrayOf(
        "Electronics", "Clothing", "Food", "Books", "Home", "Beauty", "Toys", "Sports", "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database helper
        dbHelper = ProductDatabaseHelper(this)

        // Get product ID from intent
        productId = intent.getLongExtra("PRODUCT_ID", -1)

        if (productId == -1L) {
            Toast.makeText(this, "Invalid product", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Setup spinner (dropdown menu)
        setupCategorySpinner()

        // Load product details
        loadProductDetails()

        // Setup button click listeners
        binding.btnUpdateProduct.setOnClickListener {
            updateProduct()
        }

        binding.btnCancelEdit.setOnClickListener {
            finish()
        }
    }

    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        binding.spinnerEditCategory.adapter = adapter
    }

    private fun loadProductDetails() {
        product = dbHelper.getProduct(productId)

        product?.let {
            binding.etEditProductName.setText(it.name)
            binding.etEditProductDescription.setText(it.description)
            binding.etEditProductPrice.setText(it.price.toString())
            binding.etEditProductQuantity.setText(it.quantity.toString())
            binding.switchEditAvailable.isChecked = it.isAvailable
            binding.etEditImageUrl.setText(it.imageUrl)

            // Set spinner selection
            val categoryPosition = categories.indexOf(it.category)
            if (categoryPosition >= 0) {
                binding.spinnerEditCategory.setSelection(categoryPosition)
            }
        } ?: run {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateProduct() {
        try {
            // Validate inputs
            val name = binding.etEditProductName.text.toString().trim()
            val description = binding.etEditProductDescription.text.toString().trim()
            val priceStr = binding.etEditProductPrice.text.toString().trim()
            val quantityStr = binding.etEditProductQuantity.text.toString().trim()
            val category = binding.spinnerEditCategory.selectedItem.toString()
            val isAvailable = binding.switchEditAvailable.isChecked
            val imageUrl = binding.etEditImageUrl.text.toString().trim()

            // Check required fields
            if (name.isEmpty()) {
                binding.etEditProductName.error = "Product name is required"
                return
            }

            if (priceStr.isEmpty()) {
                binding.etEditProductPrice.error = "Price is required"
                return
            }

            if (quantityStr.isEmpty()) {
                binding.etEditProductQuantity.error = "Quantity is required"
                return
            }

            // Parse numeric values
            val price = priceStr.toDouble()
            val quantity = quantityStr.toInt()

            // Create updated product object
            val updatedProduct = Product(
                id = productId,
                name = name,
                description = description,
                price = price,
                quantity = quantity,
                category = category,
                isAvailable = isAvailable,
                imageUrl = imageUrl
            )

            // Update in database
            val result = dbHelper.updateProduct(updatedProduct)

            if (result > 0) {
                Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}