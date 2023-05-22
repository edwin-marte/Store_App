package com.example.store_app.ui.view

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.store_app.data.model.User
import com.example.store_app.ui.component.MyRadioButton
import com.example.store_app.presentation.MainViewModel
import com.google.gson.Gson

@Composable
fun AccountCreationView(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "ACCOUNT CREATION", style = MaterialTheme.typography.h6)

            fun onClickBody(radioOptions: List<String> = listOf(), option: Int = 1) {
                if (radioOptions[option] == radioOptions[0]) {
                    navController.navigate("account_view")
                } else if (radioOptions[option] == radioOptions[1]) {
                    navController.navigate("login_view")
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyRadioButton(
                    option = 0,
                    radioOptions = viewModel.radioOptions,
                    selectedOption = viewModel.radioButtonState.component1(),
                    onOptionSelected = viewModel.radioButtonState.component2(),
                    onClickBody = ::onClickBody
                )
                MyRadioButton(
                    option = 1,
                    radioOptions = viewModel.radioOptions,
                    selectedOption = viewModel.radioButtonState.component1(),
                    onOptionSelected = viewModel.radioButtonState.component2(),
                    onClickBody = ::onClickBody
                )
            }

            OutlinedTextField(
                value = viewModel.emailText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                label = { Text(text = "Email") },
                onValueChange = { viewModel.emailText = it }
            )

            OutlinedTextField(
                value = viewModel.usernameText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                label = { Text(text = "Username") },
                onValueChange = { viewModel.usernameText = it }
            )

            OutlinedTextField(
                value = viewModel.passwordText,
                onValueChange = { viewModel.passwordText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                label = { Text(text = "Password") },
                visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (viewModel.passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (viewModel.passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {viewModel.passwordVisible = !viewModel.passwordVisible}) {
                        Icon(imageVector  = image, description)
                    }
                }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 40.dp, end = 40.dp),

                onClick = {
                    val email = viewModel.emailText.text
                    val password = viewModel.passwordText.text

                    if (email.isEmpty() || email.isBlank()) {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    if (password.isEmpty() || password.isBlank()) {
                        Toast.makeText(context, "Please enter a valid password", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (password.length < 6) {
                        Toast.makeText(context, "The password must have at least 6 characters", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    val user = User(
                        email = viewModel.emailText.text,
                        username = viewModel.usernameText.text,
                        password = viewModel.passwordText.text
                    )
                    val param = Uri.encode(Gson().toJson(user))
                    navController.navigate("personal_info_view/${param}")
                }

            ) {
                Text(text = "NEXT")
            }
        }
    }
}

@Preview()
@Composable
private fun DefaultPreview() {
    //AccountCreationView()
}