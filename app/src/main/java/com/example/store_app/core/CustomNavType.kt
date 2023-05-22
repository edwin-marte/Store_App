package com.example.store_app.core

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson

object CustomNavType {
    inline fun <reified T> navType(): NavType<T> {
        return object : NavType<T>(
            isNullableAllowed = false
        ) {
            override fun put(bundle: Bundle, key: String, value: T) {
                bundle.putParcelable(key, value as Parcelable)
            }
            override fun get(bundle: Bundle, key: String): T {
                return bundle.getParcelable(key)!!
            }
            override fun parseValue(value: String): T {
                return Gson().fromJson(value, T::class.java)
            }
        }
    }
}