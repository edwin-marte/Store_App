package com.example.store_app.ui.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.store_app.data.model.Product
import com.google.gson.Gson

@Composable
fun ProductRecyclerViewRow(item: Product, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    val product = Uri.encode(Gson().toJson(item))
                    navController.navigate("details_view/${product}")
                }
            ),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(item.image),
                contentDescription = "product image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(64.dp)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = item.title, style = MaterialTheme.typography.h6)
                Row {
                    Text(text = "Price: ${item.price}", style = MaterialTheme.typography.caption)
                    Spacer(Modifier.weight(1f))
                    Text(text = item.rating.toString(), style = MaterialTheme.typography.caption)
                }
            }
        }
    }
}