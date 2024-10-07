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
import com.example.colllette.ui.CartScreen
import com.example.colllette.ui.LoginScreen
import com.example.colllette.ui.ProductListingScreen
import com.example.colllette.ui.theme.ActivationPendingScreen
import com.example.colllette.ui.theme.CollletteTheme
import com.example.colllette.ui.theme.HomeScreen
import com.example.colllette.viewmodel.ProductViewModel
import com.example.colllette.viewmodel.ProductViewModelFactory

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

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("activationPending") { ActivationPendingScreen() }
        composable("home") { HomeScreen() }
        composable("productListing") {
            ProductListingScreen(
                productViewModel = productViewModel,
                onNavigateToCart = { navController.navigate("cart") }
            )
        }
        composable("cart") {
            CartScreen(
                productViewModel = productViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}