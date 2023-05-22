package com.example.store_app.data.remote

import com.example.store_app.data.model.Cart
import com.example.store_app.data.model.Product
import com.example.store_app.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WebService {
    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getSingleProduct(@Path("id") id: String): Product

    @GET("carts/{id}")
    suspend fun getCart(@Path("id") id: String): Cart

    @POST("carts")
    suspend fun insertCart(@Body cart: Cart): Cart

    @POST("users")
    suspend fun insertUser(@Body user: User): User

    @PUT("carts/{id}")
    suspend fun updateCart(@Path("id") id: String, @Body cart: Cart)

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User)
}