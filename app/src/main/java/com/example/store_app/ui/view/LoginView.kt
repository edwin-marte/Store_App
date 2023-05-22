package com.example.store_app.ui.view

import android.content.Context
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
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import com.example.store_app.data.model.User
import com.example.store_app.ui.component.MyRadioButton
import com.example.store_app.presentation.MainViewModel
import com.example.store_app.core.Resource
import com.example.store_app.core.UserDataStore

@Composable
fun LoginView(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current

    val emailToken = stringPreferencesKey("email")
    val passwordToken = stringPreferencesKey("password")
    val store = UserDataStore(context)

    LaunchedEffect(key1 = context) {
        viewModel.firebaseUserEvent.collect { event ->
            when(val resource = event.resource) {
                is Resource.Success -> {
                    store.saveToken(resource.data.email, emailToken)
                    store.saveToken(resource.data.password, passwordToken)
                    navController.navigate("main_view/${resource.data.email}")
                }

                is Resource.Failure -> {
                    Toast.makeText(context, resource.exception.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    var loadContent by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = context) {
        store.getAccessToken(emailToken).collect { email ->
            store.getAccessToken(passwordToken).collect { password ->
                if (email != "" && password != "") {
                    viewModel.logIn(user = User(email = email!!, password = password!!))
                } else {
                    loadContent = true
                }
            }
        }
    }

    if (loadContent) {
        LoginContent(navController, viewModel, context)
    }
}

@Composable
private fun LoginContent(navController: NavController, viewModel: MainViewModel, context: Context) {
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
            Text(text = "WELCOME", style = MaterialTheme.typography.h6)

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

            OutlinedTextField(value = viewModel.emailText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                label = { Text(text = "Email") },
                onValueChange = { viewModel.emailText = it })

            OutlinedTextField(value = viewModel.passwordText,
                onValueChange = { viewModel.passwordText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 40.dp, end = 40.dp, bottom = 0.dp),
                label = { Text(text = "Password") },
                visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (viewModel.passwordVisible) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (viewModel.passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { viewModel.passwordVisible = !viewModel.passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                })

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 40.dp, end = 40.dp),

                onClick = {
                    val email = viewModel.emailText.text
                    val password = viewModel.passwordText.text

                    if (email.isEmpty() || email.isBlank()) {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_LONG)
                            .show()
                        return@Button
                    }
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_LONG)
                            .show()
                        return@Button
                    }

                    if (password.isEmpty() || password.isBlank() || password.length < 6) {
                        Toast.makeText(context, "Please enter a valid password", Toast.LENGTH_LONG)
                            .show()
                        return@Button
                    }

                    viewModel.logIn(user = User(email = email, password = password))
                }) {
                Text(text = "LOGIN")
            }
        }
    }
}
