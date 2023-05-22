package com.example.store_app.domain

import com.example.store_app.core.ResourceState
import com.example.store_app.data.model.Cart
import com.example.store_app.data.model.CartProduct
import com.example.store_app.data.model.Product
import com.example.store_app.data.model.User
import com.example.store_app.core.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    //FakeStore API
    suspend fun getAllProducts(): Resource<List<Product>>
    suspend fun getCart(id: String): Resource<Cart>
    suspend fun getSingleProduct(id: String): Flow<ResourceState<Product>>
    suspend fun insertCart(cart: Cart): Flow<ResourceState<Cart>>
    suspend fun insertUser(user: User): Flow<ResourceState<User>>

    //Firebase
    suspend fun getCartByUserId(): Flow<ResourceState<Cart>>
    suspend fun requestCartCreation(cart: Cart): Flow<ResourceState<Cart>>
    suspend fun deleteCartProduct(product: CartProduct): Flow<ResourceState<String>>
    suspend fun addProductToCart(cartProduct: CartProduct): Flow<ResourceState<Cart>>
    suspend fun requestLogIn(user: User): Flow<ResourceState<User>>
    suspend fun requestSignUp(user: User): Flow<ResourceState<User>>
    suspend fun requestSignOut(): Resource<Unit>
}