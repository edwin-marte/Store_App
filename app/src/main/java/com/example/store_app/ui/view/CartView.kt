package com.example.store_app.ui.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.example.store_app.data.model.CartProduct
import com.example.store_app.presentation.MainViewModel
import com.example.store_app.ui.component.CartProductRecyclerViewRow
import com.example.store_app.ui.theme.*
import com.example.store_app.core.Resource

@Composable
fun CartView(viewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    val flow = remember(viewModel.cartState, lifecycleOwner) {
        viewModel.cartState.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val resource = flow.collectAsState(Resource.Loading()).value

    LaunchedEffect(true) {
        viewModel.fetchCartByUserId()
    }

    when(resource) {
        is Resource.Loading -> Loading()
        is Resource.Success -> {
            Screen(resource.data.products, viewModel, navController)
            Log.d("aver", "here")
        }
        is Resource.Failure -> {
            Toast.makeText(context, "Error: ${resource.exception.message}",
                Toast.LENGTH_SHORT).show()
        }
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
    data: List<CartProduct>,
    viewModel: MainViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            elevation = null,
            onClick = {
                viewModel.resetTotal()
                navController.navigateUp()
            }
        ) {
            Text(text = "Back", style = MaterialTheme.typography.h6)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(data) { item ->
                    val product = viewModel.fetchSingleProduct(item.productId.toString()).observeAsState().value

                    if (product is Resource.Success){
                        CartProductRecyclerViewRow(product.data, item, viewModel)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = "Total: ${viewModel.totalState.collectAsState().value}",
                        style = MaterialTheme.typography.h6
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .padding(bottom = 10.dp)
                ) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 4.dp,
                            disabledElevation = 0.dp
                        ),
                        border = BorderStroke(1.dp, LightGray),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "BUY", style = MaterialTheme.typography.h6)
                    }
                }
            }
        }
    }
}