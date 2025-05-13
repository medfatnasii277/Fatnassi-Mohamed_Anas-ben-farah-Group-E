package com.example.productprojectandroid.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.productprojectandroid.database.ProductDatabaseHelper
import com.example.productprojectandroid.databinding.ActivityProductDetailBinding
import com.example.productprojectandroid.models.Product


class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var dbHelper: ProductDatabaseHelper
    private var productId: Long = -1
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShareProduct.setOnClickListener {
            shareProduct()
        }



        // Initialize the database helper
        dbHelper = ProductDatabaseHelper(this)

        // Get product ID from intent
        productId = intent.getLongExtra("PRODUCT_ID", -1)

        if (productId == -1L) {
            Toast.makeText(this, "Invalid product", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load product details
        loadProductDetails()

        // Setup button click listeners
        binding.btnEditProduct.setOnClickListener {
            editProduct()
        }

        binding.btnDeleteProduct.setOnClickListener {
            confirmDelete()
        }
    }


    private fun shareProduct() {
        product?.let {
            val shareText = """
            📢 Check out this product!
            🏷 Name: ${it.name}
            📄 Description: ${it.description}
            💰 Price: $${it.price}
            📦 Quantity: ${it.quantity}
            📂 Category: ${it.category}
            ✅ Availability: ${if (it.isAvailable) "Available" else "Out of Stock"}
        """.trimIndent()

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }







    private fun loadProductDetails() {
        product = dbHelper.getProduct(productId)

        product?.let {
            binding.tvProductName.text = it.name
            binding.tvProductDescription.text = it.description
            binding.tvProductPrice.text = "$${it.price}"
            binding.tvProductQuantity.text = it.quantity.toString()
            binding.tvProductCategory.text = it.category
            binding.tvProductAvailability.text = if (it.isAvailable) "Available" else "Out of Stock"
        } ?: run {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun editProduct() {
        val intent = Intent(this, EditProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton("Yes") { _, _ ->
                deleteProduct()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteProduct() {
        val result = dbHelper.deleteProduct(productId)

        if (result > 0) {
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadProductDetails()
    }
}