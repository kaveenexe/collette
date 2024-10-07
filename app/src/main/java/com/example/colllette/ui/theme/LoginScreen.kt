package com.example.colllette.ui

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colllette.R
import com.example.colllette.ui.theme.customBlue
import com.example.colllette.viewmodel.AuthViewModel
import com.example.colllette.viewmodel.AuthViewModelFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context.applicationContext as Application)
    )

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authResponse by authViewModel.authResponse.collectAsState()
    val error by authViewModel.error.collectAsState()
    val inactiveAccount by authViewModel.inactiveAccount.collectAsState()

    // Scaffold to handle Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        // Background with centered content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .wrapContentSize(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Add a logo or icon at the top
                Image(
                    painter = painterResource(id = R.drawable.nonbglogo), // Replace with your logo
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(160.dp)
                        .padding(bottom = 10.dp)
                )

                // Title
                Text(
                    text = "Welcome Back",
                    fontSize = 24.sp,
                    color = customBlue,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                // Username Input
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                // Password Input
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                // Login Button
                Button(
                    onClick = { authViewModel.login(username, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = customBlue)
                ) {
                    Text("Login", fontSize = 16.sp, color = Color.White)
                }

                // Error message
                error?.let {
                    Text(
                        text = "Error: $it",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Navigate to Login
                Text(
                    text = "Don't have an account? Register here",
                    color = customBlue,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { navController.navigate("register") },
                    textAlign = TextAlign.Center
                )
            }

            // Handle navigation based on login state
            // Handle navigation based on login state
            LaunchedEffect(authResponse, inactiveAccount) {
                when {
                    inactiveAccount -> {
                        navController.navigate("activationPending") {
                            popUpTo("login") { inclusive = true }
                        }
                        authViewModel.resetInactiveAccount()
                        // Optionally, show a Snackbar message
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Account is inactive. Please activate your account.")
                        }
                        println("LoginScreen: Navigated to ActivationPendingScreen")
                    }
                    authResponse != null -> {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Welcome, ${authResponse?.firstName}!")
                        }
                        authViewModel.resetAuthResponse()
                        println("LoginScreen: Navigated to HomeScreen")
                    }
                }
            }
        }
    }
}
