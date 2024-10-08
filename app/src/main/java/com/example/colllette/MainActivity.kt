package com.example.colllette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.colllette.data.local.AppDatabase
import com.example.colllette.network.ApiClient
import com.example.colllette.repositories.UserRepository
import com.example.colllette.ui.theme.ActivationPendingScreen
import com.example.colllette.ui.RegistrationScreen
import com.example.colllette.ui.CartScreen
import com.example.colllette.ui.LoginScreen
import com.example.colllette.ui.ProductDetailsScreen
import com.example.colllette.ui.ProductListingScreen
import com.example.colllette.ui.ProfileScreen
import com.example.colllette.ui.theme.CollletteTheme
import com.example.colllette.viewmodel.ProductViewModel
import com.example.colllette.viewmodel.ProductViewModelFactory
import com.example.colllette.viewmodel.UserViewModel
import com.example.colllette.viewmodel.UserViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollletteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CollletteApp()
                }
            }
        }
    }
}

@Composable
fun CollletteApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val productViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(context.applicationContext as android.app.Application)
    )
    // Initialize ApiClient
    val apiClient = ApiClient(context.applicationContext)

    // Initialize UserRepository inside the UserViewModelFactory
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            application = context.applicationContext as android.app.Application
        )
    )

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("activationPending") { ActivationPendingScreen(navController) }
        composable("registration") { RegistrationScreen(navController) }
        composable("home") { ProductListingScreen(
            productViewModel = productViewModel,
            onNavigateToCart = { navController.navigate("cart") },
            onNavigateToProductDetails = { productId ->
                navController.navigate("productDetails/$productId")
            },
            onNavigateToProfile = { navController.navigate("profile") } // Navigate to ProfileScreen
        ) }

        composable("cart") {
            CartScreen(
                productViewModel = productViewModel,
                onNavigateBack = { navController.popBackStack() },
                onProceedToCheckout = { /* Implement checkout navigation */ }
            )
        }
        composable("productDetails/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            productId?.let { productViewModel.fetchProductDetails(it) }
            ProductDetailsScreen(
                productViewModel = productViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("profile") {
            ProfileScreen(navController, userViewModel = userViewModel)
        }
    }
}