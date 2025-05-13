package com.example.productprojectandroid.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productprojectandroid.adapters.ProductAdapter
import com.example.productprojectandroid.database.ProductDatabaseHelper
import com.example.productprojectandroid.databinding.ActivityProductListBinding
import com.example.productprojectandroid.models.Product


class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var dbHelper: ProductDatabaseHelper
    private lateinit var productAdapter: ProductAdapter
    private var productList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database helper
        dbHelper = ProductDatabaseHelper(this)

        // Setup RecyclerView
        setupRecyclerView()

        // Set up FAB click listener
        binding.fabAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(productList) { product ->
            // Handle item click
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            startActivity(intent)
        }

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProductListActivity)
            adapter = productAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        productList = dbHelper.getAllProducts()

        if (productList.isEmpty()) {
            Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show()
        }

        productAdapter.updateList(productList)
    }
}