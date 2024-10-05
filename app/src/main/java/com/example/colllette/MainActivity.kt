package com.example.colllette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.colllette.ui.LoginScreen
import androidx.navigation.compose.rememberNavController
import com.example.colllette.ui.theme.ActivationPendingScreen
import com.example.colllette.ui.theme.HomeScreen
import com.example.colllette.ui.theme.RegistrationScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController) }
                composable("activationPending") { ActivationPendingScreen() }
                composable("home") { HomeScreen() }
                // Add other destinations
            }

        }
    }
}