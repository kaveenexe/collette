package com.example.colllette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.colllette.ui.LoginScreen
import androidx.navigation.compose.rememberNavController
import com.example.colllette.ui.OrderCheckoutScreen
import com.example.colllette.ui.OrderHistoryScreen
import com.example.colllette.ui.OrderSuccessScreen
import com.example.colllette.ui.PaymentScreen
import com.example.colllette.ui.ViewOrderScreen
import com.example.colllette.ui.ProductListingScreen
import com.example.colllette.ui.theme.ActivationPendingScreen
import com.example.colllette.ui.theme.HomeScreen
import com.example.colllette.ui.theme.RegistrationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController)}
                composable("products") { ProductListingScreen() }
                composable("activationPending") { ActivationPendingScreen() }
                composable("home") { HomeScreen() }
                composable("checkout") { OrderCheckoutScreen(navController)}
                composable("payment") { PaymentScreen(navController)}
                composable("success") {OrderSuccessScreen(navController)}
                composable("order") { ViewOrderScreen(navController)}
                composable("history") { OrderHistoryScreen(navController)}
                // Add other destinations
            }

        }
    }
}