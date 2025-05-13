package com.example.productprojectandroid.adapters



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.productprojectandroid.databinding.ProductItemBinding
import com.example.productprojectandroid.models.Product


class ProductAdapter(
    private var productList: ArrayList<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        with(holder.binding) {
            productName.text = product.name
            productPrice.text = "Price: $${product.price}"
            productCategory.text = "Category: ${product.category}"
            productQuantity.text = "Quantity: ${product.quantity}"
            productStatus.text = if (product.isAvailable) "Available" else "Out of Stock"

            // Set click listener
            root.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    override fun getItemCount(): Int = productList.size

    fun updateList(newList: ArrayList<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}
