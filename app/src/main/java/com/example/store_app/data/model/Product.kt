package com.example.store_app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("category")
    val category: String = "",
    @SerializedName("image")
    var image: String = "",
    @SerializedName("rating")
    val rating: Rating = Rating()
): Parcelable

@Parcelize
data class Rating(
    @SerializedName("rate")
    val rate: Double = 0.0,
    @SerializedName("count")
    val count: Double = 0.0
): Parcelable {
    override fun toString(): String {
        return "Rate: $rate, Available: $count"
    }
}