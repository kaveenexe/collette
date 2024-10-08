// ProfileScreen.kt
package com.example.colllette.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colllette.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colllette.data.local.AppDatabase
import com.example.colllette.network.ApiClient
import com.example.colllette.repositories.UserRepository
import com.example.colllette.ui.theme.customBlue
import com.example.colllette.ui.theme.customRed
import com.example.colllette.viewmodel.UserViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            application = LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    val user by userViewModel.user.collectAsState()
    val updateStatus by userViewModel.updateStatus.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // State variables for editable fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }

    // Initialize fields when user data is available
    LaunchedEffect(user) {
        user?.let { currentUser ->
            firstName = currentUser.firstName
            lastName = currentUser.lastName
            username = currentUser.username
            address = currentUser.address
            isActive = currentUser.isActive
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text ="My Profile", color = customBlue) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("home") // Navigate to the profile screen
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home, // Change to a person icon or any profile icon
                            contentDescription = "Home",
                            tint = customBlue
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
                            contentDescription = "Logout",
                            tint = customRed
                        )
                    }
                }
            )
        },

        ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (user != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    // Editable fields
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Address") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(text = "Active:", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isActive,
                            onCheckedChange = { isActive = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            userViewModel.updateUser(
                                firstName = firstName,
                                lastName = lastName,
                                username = username,
                                address = address,
                                isActive = isActive
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = customBlue),
                        enabled = updateStatus != UserViewModel.UpdateStatus.Loading
                    ) {
                        if (updateStatus is UserViewModel.UpdateStatus.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Save Changes", fontSize = 16.sp, color = Color.White)
                        }
                    }
                }
            } else {
                // When 'user' is null, display an error message
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "User data not available.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Handle update status
            LaunchedEffect(updateStatus) {
                when (updateStatus) {
                    is UserViewModel.UpdateStatus.Success -> {
                        snackbarHostState.showSnackbar("Profile updated successfully")
                        userViewModel.resetUpdateStatus()
                    }
                    is UserViewModel.UpdateStatus.Error -> {
                        snackbarHostState.showSnackbar("Error: ${(updateStatus as UserViewModel.UpdateStatus.Error).message}")
                        userViewModel.resetUpdateStatus()
                    }
                    else -> {}
                }
            }
        }
    }
}
