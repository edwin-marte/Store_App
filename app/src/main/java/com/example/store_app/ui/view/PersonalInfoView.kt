package com.example.store_app.ui.view

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.store_app.data.model.Name
import com.example.store_app.data.model.User
import com.example.store_app.presentation.MainViewModel
import com.google.gson.Gson

@Composable
fun PersonalInfoView(user: User, navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "PERSONAL INFO.", style = MaterialTheme.typography.h6)

            OutlinedTextField(
                value = viewModel.firstNameText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                label = { Text(text = "First Name") },
                onValueChange = { viewModel.firstNameText = it }
            )

            OutlinedTextField(
                value = viewModel.lastNameText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                label = { Text(text = "Last Name") },
                onValueChange = { viewModel.lastNameText = it }
            )

            OutlinedTextField(
                value = viewModel.phoneText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Phone") },
                onValueChange = { viewModel.phoneText = it }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 40.dp, end = 40.dp),
                onClick = {
                    val firstName = viewModel.firstNameText.text
                    val lastName = viewModel.lastNameText.text
                    val phone = viewModel.phoneText.text

                    if (firstName.isEmpty() || firstName.isBlank()) {
                        Toast.makeText(context, "Please enter a valid name", Toast.LENGTH_LONG)
                            .show()
                        return@Button
                    }
                    if (lastName.isEmpty() || lastName.isBlank()) {
                        Toast.makeText(context, "Please enter a valid last name", Toast.LENGTH_LONG)
                            .show()
                        return@Button
                    }
                    if (phone.isEmpty() || phone.isBlank()) {
                        Toast.makeText(context, "Please enter a valid phone", Toast.LENGTH_LONG)
                            .show()
                        return@Button
                    }
                    if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
                        Toast.makeText(context, "Please enter a valid phone", Toast.LENGTH_LONG)
                            .show()
                        return@Button
                    }

                    user.name = Name(
                        viewModel.firstNameText.text,
                        viewModel.lastNameText.text
                    )
                    user.phone = viewModel.phoneText.text

                    val param = Uri.encode(Gson().toJson(user))
                    navController.navigate("address_view/${param}")
                }

            ) {
                Text(text = "NEXT")
            }
        }
    }
}