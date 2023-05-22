package com.example.store_app.ui.view

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import com.example.store_app.data.model.Product
import com.example.store_app.presentation.MainViewModel
import com.example.store_app.ui.component.ProductRecyclerViewRow
import com.example.store_app.ui.theme.*
import com.example.store_app.core.Resource

@Composable
fun MainView(email: String, viewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = context) {
        viewModel.fetchAllProducts()
    }

    when (val resource = state.value) {
        is Resource.Loading -> Loading()
        is Resource.Success -> {
            Screen(
                data = resource.data,
                navController = navController,
                context = context,
                viewModel = viewModel,
                email = email
            )
        }
        is Resource.Failure -> {}
    }
}

@Composable
private fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = SkyBlue,
            strokeWidth = 8.dp
        )
    }
}

@Composable
private fun Screen(
    data: List<Product>,
    navController: NavController,
    context: Context,
    viewModel: MainViewModel,
    email: String
) {
    val emailToken = stringPreferencesKey("email")
    val passwordToken = stringPreferencesKey("password")

    when (viewModel.emailTokenState.resource) {
        is Resource.Success -> {
            when (viewModel.passwordTokenState.resource) {
                is Resource.Success -> {
                    when (viewModel.signOutState.resource) {
                        is Resource.Success -> {
                            navController.navigate("login_view")
                        }
                        else -> {}
                    }
                }
                else -> {}
            }
        }
        else -> {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(text = email)

                    Button(
                        onClick = {
                            navController.navigate("cart_view")
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 2.dp,
                            disabledElevation = 0.dp
                        ),
                        border = BorderStroke(1.dp, LightGray),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(text = "Cart", style = MaterialTheme.typography.h6)
                    }
                }

                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    elevation = null,
                    onClick = {
                        viewModel.clearDataStore(emailToken, context)
                        viewModel.clearDataStore(passwordToken, context)
                        viewModel.signOut()
                    }
                ) {
                    Text(text = "Sign Out", style = MaterialTheme.typography.h6)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(data) { item ->
                        ProductRecyclerViewRow(item, navController)
                    }
                }
            }
        }
    }
}