package com.example.store_app.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.store_app.data.model.CartProduct
import com.example.store_app.data.model.Product
import com.example.store_app.presentation.MainViewModel
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsView(product: Product, navController: NavController, viewModel: MainViewModel) {
    BoxWithConstraints {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    elevation = null,
                    onClick = {
                        navController.navigateUp()
                    }
                ) {
                    Text(text = "Back", style = MaterialTheme.typography.h6)
                }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                        Text(text = product.title, style = MaterialTheme.typography.h5)
                        Text(text = product.category, style = MaterialTheme.typography.h6)
                    }

                    Image(
                        painter = rememberAsyncImagePainter(product.image),
                        contentDescription = "product image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = product.rating.toString())
                    }
                    Text(text = "$${ product.price }", style = MaterialTheme.typography.h6)
                    Text(text = product.description)

                    val options = listOf("1", "2", "3", "4", "5", "6")

                    var expanded by remember { mutableStateOf(false) }
                    var selectedOptionText by remember { mutableStateOf(options[0]) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        modifier = Modifier.width(120.dp).height(55.dp),
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        TextField(
                            readOnly = true,
                            value = selectedOptionText,
                            onValueChange = { },
                            label = { Text("Quantity") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            options.forEach { selectionOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedOptionText = selectionOption
                                        expanded = false
                                    }
                                ) {
                                    Text(text = selectionOption)
                                }
                            }
                        }
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.addProductToCart(
                                CartProduct(
                                    productId = product.id,
                                    quantity = selectedOptionText.toInt()
                                )
                            )
                            navController.navigateUp()
                        }
                    ) {
                        Text(text = "Add To Cart")
                    }
                }
            }
        }
    }
}