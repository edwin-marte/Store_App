package com.example.store_app.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.store_app.data.model.Address
import com.example.store_app.data.model.Cart
import com.example.store_app.data.model.User
import com.example.store_app.presentation.MainViewModel
import com.example.store_app.core.Resource

@Composable
fun AddressCreationView(user: User, viewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.firebaseUserEvent.collect { event ->
            when(val resource = event.resource) {
                is Resource.Success -> {
                    val cart = Cart(userId = resource.data.id)
                    viewModel.createCart(cart)
                }

                is Resource.Failure -> {
                    Toast.makeText(context, resource.exception.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(key1 = context) {
        viewModel.firebaseCartEvent.collect { event ->
            when(val resource = event.resource) {
                is Resource.Success -> {
                    navController.navigate("main_view")
                }

                is Resource.Failure -> {
                    Toast.makeText(context, resource.exception.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "PERSONAL ADDRESS", style = MaterialTheme.typography.h6)

            OutlinedTextField(
                value = viewModel.cityText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                label = { Text(text = "City") },
                onValueChange = { viewModel.cityText = it }
            )

            OutlinedTextField(
                value = viewModel.streetText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                label = { Text(text = "Street") },
                onValueChange = { viewModel.streetText = it }
            )

            OutlinedTextField(value = viewModel.numberText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                label = { Text(text = "House Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.numberText = it }
            )

            OutlinedTextField(
                value = viewModel.zipcodeText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Zipcode") },
                onValueChange = { viewModel.zipcodeText = it }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 40.dp, end = 40.dp),

                onClick = {
                    val city = viewModel.cityText.text
                    val street = viewModel.streetText.text
                    val number = viewModel.numberText.text
                    val zipcode = viewModel.zipcodeText.text

                    if (city.isEmpty() || city.isBlank()) {
                        Toast.makeText(context, "Please enter a valid city", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (street.isEmpty() || street.isBlank()) {
                        Toast.makeText(context, "Please enter a valid last street", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (number.isEmpty() || number.isBlank()) {
                        Toast.makeText(context, "Please enter a valid house number", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (zipcode.isEmpty() || zipcode.isBlank()) {
                        Toast.makeText(context, "Please enter a valid zipcode", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    user.address = Address(
                        city = viewModel.cityText.text,
                        street = viewModel.streetText.text,
                        number = viewModel.numberText.text.toInt(),
                        zipcode = viewModel.zipcodeText.text
                    )

                    viewModel.createUser(user)
                }
            ) {
                Text(text = "REGISTER")
            }
        }
    }
}