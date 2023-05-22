package com.example.store_app.domain

import com.example.store_app.core.ResourceState
import com.example.store_app.data.model.Cart
import com.example.store_app.data.model.CartProduct
import com.example.store_app.data.model.Product
import com.example.store_app.data.model.User
import com.example.store_app.core.Resource
import com.example.store_app.data.remote.NetworkDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val networkDataSource: NetworkDataSource): Repository {
    //FakeStore API
    override suspend fun getAllProducts(): Resource<List<Product>> {
        return Resource.Success(networkDataSource.webService.getAllProducts())
    }

    override suspend fun getCart(id: String): Resource<Cart> {
        return Resource.Success(networkDataSource.webService.getCart(id))
    }

    override suspend fun getSingleProduct(id: String): Flow<ResourceState<Product>> = callbackFlow {
        try {
            trySend(
                ResourceState(
                    resource = Resource.Success(networkDataSource.webService.getSingleProduct(id))
                )
            )
        } catch (e: Exception) {
            trySend(ResourceState(Resource.Failure(e)))
        }
        awaitClose { close() }
    }

    override suspend fun insertCart(cart: Cart): Flow<ResourceState<Cart>> = callbackFlow {
        try {
            trySend(
                ResourceState(
                    resource = Resource.Success(networkDataSource.webService.insertCart(cart))
                )
            )
        } catch (e: Exception) {
            trySend(ResourceState(Resource.Failure(e)))
        }
        awaitClose { close() }
    }

    override suspend fun insertUser(user: User): Flow<ResourceState<User>> = callbackFlow {
        try {
            trySend(
                ResourceState(
                    resource = Resource.Success(networkDataSource.webService.insertUser(user))
                )
            )
        } catch (e: Exception) {
            trySend(ResourceState(Resource.Failure(e)))
        }
        awaitClose { close() }
    }

    //Firebase
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userDatabase = FirebaseDatabase.getInstance().getReference("Users")
    private val cartsDatabase = FirebaseDatabase.getInstance().getReference("Carts")

    override suspend fun getCartByUserId(): Flow<ResourceState<Cart>> = callbackFlow {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val products = mutableListOf<CartProduct>()
                for (ds in dataSnapshot.child("products").children) {
                    val product = ds.getValue(CartProduct::class.java)!!
                    products.add(product)
                }

                val cart = Cart(
                    userId = dataSnapshot.child("userId").getValue(String::class.java)!!,
                    date = dataSnapshot.child("date").getValue(String::class.java)!!,
                    products = products
                )

                trySend(ResourceState(resource = Resource.Success(cart)))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                trySend(ResourceState(resource = Resource.Failure(databaseError.toException())))
            }
        }

        cartsDatabase.child(firebaseAuth.currentUser!!.uid).addValueEventListener(postListener)
        awaitClose { close() }
    }

    override suspend fun deleteCartProduct(product: CartProduct): Flow<ResourceState<String>> =
        callbackFlow {
            cartsDatabase.child(firebaseAuth.currentUser!!.uid).child("products")
                .child(product.cartProductId)
                .removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResourceState(resource = Resource.Success("Success")))
                    }
                }
                .addOnFailureListener {
                    trySend(ResourceState(resource = Resource.Failure(it)))
                }
            awaitClose { close() }
        }

    override suspend fun addProductToCart(
        cartProduct: CartProduct
    ): Flow<ResourceState<Cart>> = callbackFlow {
        val productsDb = cartsDatabase.child(firebaseAuth.currentUser!!.uid).child("products")

        val key: String = productsDb.push().key!!
        val map: MutableMap<String, Any> = HashMap()
        cartProduct.cartProductId = key
        map[key] = cartProduct
        productsDb.updateChildren(map)

        awaitClose { close() }
    }

    override suspend fun requestCartCreation(cart: Cart): Flow<ResourceState<Cart>> = callbackFlow {
        cartsDatabase.child(firebaseAuth.currentUser!!.uid).setValue(cart).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(ResourceState(resource = Resource.Success(cart)))
            }
        }.addOnFailureListener { trySend(ResourceState(Resource.Failure(it))) }
        awaitClose { close() }
    }

    override suspend fun requestLogIn(user: User): Flow<ResourceState<User>> = callbackFlow {
        firebaseAuth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(ResourceState(resource = Resource.Success(user)))
            }
        }.addOnFailureListener {
            trySend(ResourceState(Resource.Failure(it)))
        }
        awaitClose { close() }
    }

    override suspend fun requestSignUp(user: User): Flow<ResourceState<User>> = callbackFlow {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user.id = firebaseAuth.currentUser!!.uid
                    userDatabase.child(firebaseAuth.currentUser!!.uid).setValue(user)
                        .addOnCompleteListener { db ->
                            if (db.isSuccessful) {
                                trySend(ResourceState(resource = Resource.Success(user)))
                            }
                        }
                        .addOnFailureListener { e -> trySend(ResourceState(Resource.Failure(e))) }
                }
            }
            .addOnFailureListener {
                trySend(ResourceState(Resource.Failure(it)))
            }
        awaitClose { close() }
    }

    override suspend fun requestSignOut(): Resource<Unit> {
        return Resource.Success(firebaseAuth.signOut())
    }
}