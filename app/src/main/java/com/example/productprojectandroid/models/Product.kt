package com.example.productprojectandroid.models

data class Product(
    var id: Long = -1,
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var quantity: Int = 0,
    var category: String = "",
    var isAvailable: Boolean = true,
    var imageUrl: String = ""
)