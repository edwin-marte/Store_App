package com.example.store_app.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.store_app.data.model.CartProduct
import com.example.store_app.data.model.Product
import com.example.store_app.presentation.MainViewModel

@Composable
fun CartProductRecyclerViewRow(product: Product, item: CartProduct, viewModel: MainViewModel) {
    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.addToTotal(product.price * item.quantity)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.h6
                )

                Row {
                    Image(
                        painter = rememberAsyncImagePainter(product.image),
                        contentDescription = "product image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(64.dp)
                    )

                    Column {
                        Text(
                            text = "PRICE: ${product.price}",
                            style = MaterialTheme.typography.caption
                        )
                        Text(
                            text = "QUANTITY: ${item.quantity}",
                            style = MaterialTheme.typography.caption
                        )
                        Text(
                            text = "TOTAL: ${product.price * item.quantity}",
                            style = MaterialTheme.typography.caption
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        elevation = null,
                        onClick = {
                            viewModel.deleteCartProduct(item)
                        }
                    ) {
                        Text(text = "Delete", style = MaterialTheme.typography.h6)
                    }
                }
            }
        }
    }
}