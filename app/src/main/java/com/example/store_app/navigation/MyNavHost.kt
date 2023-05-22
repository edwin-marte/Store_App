package com.example.store_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.store_app.data.model.Product
import com.example.store_app.data.model.User
import com.example.store_app.presentation.MainViewModel
import com.example.store_app.ui.view.*
import com.example.store_app.core.CustomNavType

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "login_view",
    mainViewModel: MainViewModel,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login_view") {
            LoginView(navController = navController, viewModel = mainViewModel)
        }

        composable("account_view") {
            AccountCreationView(navController = navController, viewModel = mainViewModel)
        }

        composable(
            route = "personal_info_view/{user}",
            arguments = listOf(
                navArgument("user") { type = CustomNavType.navType<User>() }
            )
        ) {
            PersonalInfoView(
                user = it.arguments?.getParcelable("user")!!,
                navController = navController,
                viewModel = mainViewModel
            )
        }

        composable(
            route = "address_view/{user}",
            arguments = listOf(
                navArgument(name = "user") { type = CustomNavType.navType<User>() }
            )
        ) {
            AddressCreationView(
                user = it.arguments?.getParcelable("user")!!,
                viewModel = mainViewModel,
                navController = navController
            )
        }

        composable(
            route = "main_view/{email}",
            arguments = listOf(
                navArgument(name = "email") { type = NavType.StringType }
            )
        ) {
            MainView(
                viewModel = mainViewModel,
                navController = navController,
                email = it.arguments?.getString("email")!!
            )
        }

        composable("cart_view") {
            CartView(viewModel = mainViewModel, navController = navController)
        }

        composable(
            route = "details_view/{product}",
            arguments = listOf(
                navArgument(name = "product") { type = CustomNavType.navType<Product>() }
            )
        ) {
            DetailsView(
                product = it.arguments?.getParcelable("product")!!,
                navController = navController,
                viewModel = mainViewModel
            )
        }
    }
}
