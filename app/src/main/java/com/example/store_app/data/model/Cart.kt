package com.example.store_app.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Cart (
    @SerializedName("userId")
    val userId: String = "",
    @SerializedName("date")
    val date: String = LocalDateTime.now().toString(),
    @SerializedName("products")
    val products: List<CartProduct> = listOf()
)

data class CartProduct(
    var cartProductId: String = "",
    @SerializedName("productId")
    val productId: Int = 0,
    @SerializedName("quantity")
    val quantity: Int = 0
)
