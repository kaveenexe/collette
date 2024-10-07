package com.example.colllette.ui

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colllette.viewmodel.HomeViewModel
import com.example.colllette.viewmodel.HomeViewModelFactory
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(context.applicationContext as Application)
    )

    val userState by homeViewModel.user.collectAsState()
    val user = userState

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    IconButton(onClick = {
                        homeViewModel.logout()
                        // Navigate back to login screen
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                        // Show a Snackbar message
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Logged out successfully")
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (user != null) {
                Text("Name: ${user.firstName} ${user.lastName}")
                Text("ID: ${user.id}")
                Text("Role: ${user.userType}")
                Text("Email: ${user.email}")
                Text("Address: ${user.address}")
                Text("NIC: ${user.nic}")
            } else {
                Text("User data not available.")
            }
        }
    }
}
