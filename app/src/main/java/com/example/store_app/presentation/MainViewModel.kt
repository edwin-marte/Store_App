package com.example.store_app.presentation

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.example.store_app.core.ResourceState
import com.example.store_app.data.model.Cart
import com.example.store_app.data.model.CartProduct
import com.example.store_app.data.model.User
import com.example.store_app.domain.Repository
import com.example.store_app.core.Resource
import com.example.store_app.core.UserDataStore
import com.example.store_app.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    var signOutState by mutableStateOf(ResourceState<Unit>())
        private set
    var emailTokenState by mutableStateOf(ResourceState<Preferences>())
        private set
    var passwordTokenState by mutableStateOf(ResourceState<Preferences>())
        private set

    //Component states of views
    var emailText by mutableStateOf(TextFieldValue(""))
    var passwordText by mutableStateOf(TextFieldValue(""))
    var passwordVisible by mutableStateOf(false)
    var usernameText by mutableStateOf(TextFieldValue(""))
    var cityText by mutableStateOf(TextFieldValue(""))
    var streetText by mutableStateOf(TextFieldValue(""))
    var numberText by mutableStateOf(TextFieldValue(""))
    var zipcodeText by mutableStateOf(TextFieldValue(""))
    var firstNameText by mutableStateOf(TextFieldValue(""))
    var lastNameText by mutableStateOf(TextFieldValue(""))
    var phoneText by mutableStateOf(TextFieldValue(""))

    val firebaseUserEvent = MutableSharedFlow<ResourceState<User>>()
    val firebaseCartEvent = MutableSharedFlow<ResourceState<Cart>>()

    val radioOptions = listOf("Create Account", "Log In")
    val radioButtonState = mutableStateOf(radioOptions[1])

    private val mutableState = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val state = mutableState.asStateFlow()

    private val _cartState = MutableStateFlow<Resource<Cart>>(Resource.Loading())
    val cartState = _cartState.asStateFlow()
    private val _totalState = MutableStateFlow(0.0)
    val totalState = _totalState.asStateFlow()

    fun fetchAllProducts() = viewModelScope.launch {
        mutableState.update { Resource.Loading() }
        try {
            mutableState.update { repository.getAllProducts() }
        } catch (e: Exception) {
            mutableState.update { Resource.Failure(e) }
        }
    }

    fun addToTotal(amount: Double) {
        _totalState.value += amount
    }

    fun resetTotal() {
        _totalState.value = 0.0
    }

    fun fetchSingleProduct(id: String) = liveData(Dispatchers.IO)  {
        repository.getSingleProduct(id).collect { result ->
            emit(result.resource)
        }
    }

    //Firebase
    fun logIn(user: User) = viewModelScope.launch {
        repository.requestLogIn(user).collect { result ->
            firebaseUserEvent.emit(result)
        }
    }

    fun createUser(user: User) = viewModelScope.launch {
        repository.requestSignUp(user).collect { result ->
            firebaseUserEvent.emit(result)
        }
    }

    fun createCart(cart: Cart) = viewModelScope.launch {
        repository.requestCartCreation(cart).collect { result ->
            firebaseCartEvent.emit(result)
        }
    }

    fun fetchCartByUserId() = viewModelScope.launch {
        _cartState.update { Resource.Loading() }

        try {
            repository.getCartByUserId().collectLatest {
                _cartState.value = it.resource!!
            }
        } catch (e: Exception) {
            _cartState.update { Resource.Failure(e) }
        }
    }

    fun deleteCartProduct(cartProduct: CartProduct) {
        viewModelScope.launch {
            repository.deleteCartProduct(cartProduct).collect {}
        }
    }

    fun addProductToCart(cartProduct: CartProduct) = viewModelScope.launch {
        _cartState.update { Resource.Loading() }

        try {
            repository.addProductToCart(cartProduct).collect {
                _cartState.update { it }
            }
        } catch (e: Exception) {
            _cartState.update { Resource.Failure(e) }
        }
    }

    fun signOut() = viewModelScope.launch {
        signOutState = try {
            ResourceState(resource = repository.requestSignOut())
        } catch (e: Exception) {
            ResourceState(resource = Resource.Failure(e))
        }
    }

    //Data Store
    fun clearDataStore(
        key: Preferences.Key<String>,
        context: Context
    ) {
        viewModelScope.launch {
            val store = UserDataStore(context)
            if (key.name == "email")
                emailTokenState = ResourceState(resource = store.clearDataStore(key))
            else
                passwordTokenState = ResourceState(resource = store.clearDataStore(key))
        }
    }
}