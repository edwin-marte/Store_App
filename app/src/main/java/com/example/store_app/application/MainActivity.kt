package com.example.store_app.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.store_app.navigation.MyNavHost
import com.example.store_app.ui.theme.Store_AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Store_AppTheme {
                MyNavHost(mainViewModel =  viewModel())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Store_AppTheme { }
}