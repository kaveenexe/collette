package com.example.colllette.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.colllette.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colllette.viewmodel.UserViewModelFactory
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    val user by userViewModel.user.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("home") // Navigate to the profile screen
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home, // Change to a person icon or any profile icon
                            contentDescription = "Home"
                        )
                    }

                    IconButton(onClick = {
                        userViewModel.logout()
                        // Navigate back to login screen
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }

                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },

    ) { paddingValues ->
        // Use 'let' to safely access 'user' when it's not null
        user?.let { currentUser ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Name: ${currentUser.firstName} ${currentUser.lastName}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Email: ${currentUser.email}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Role: ${currentUser.userType}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Address: ${currentUser.address}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "NIC: ${currentUser.nic}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } ?: run {
            // When 'user' is null, display an error message
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "User data not available.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
