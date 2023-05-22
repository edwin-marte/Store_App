package com.example.store_app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("username")
    var username: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("name")
    var name: Name = Name(),
    @SerializedName("address")
    var address: Address = Address(),
    @SerializedName("phone")
    var phone: String = ""
): Parcelable

@Parcelize
data class Name(
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("lastName")
    val lastName: String = ""
): Parcelable

@Parcelize
data class Address(
    @SerializedName("city")
    val city: String = "",
    @SerializedName("street")
    val street: String = "",
    @SerializedName("number")
    val number: Int = 0,
    @SerializedName("zipcode")
    val zipcode: String = ""
): Parcelable
