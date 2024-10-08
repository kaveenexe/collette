package com.example.colllette.ui

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.colllette.network.UserRegisterDto
import com.example.colllette.ui.theme.customBlue
import com.example.colllette.viewmodel.AuthViewModel
import com.example.colllette.viewmodel.AuthViewModelFactory
import androidx.compose.runtime.getValue

@Composable
fun RegistrationScreen(navController: NavController) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context.applicationContext as Application)
    )
    var nic by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }

    val error by authViewModel.error.collectAsState()

    // Add a state to control the visibility of the success dialog
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Observe the registration success state
    val registrationSuccess by authViewModel.registrationSuccess.collectAsState()

    // Check if registration was successful
    if (registrationSuccess) {
        showSuccessDialog = true
        // Reset the registration success state in the ViewModel
        authViewModel.resetRegistrationSuccess()
    }

    // Display the success dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            },
            title = { Text("Registration Successful") },
            text = { Text("Your account has been created successfully. Please wait for account activation.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Add a ScrollState
    val scrollState = rememberScrollState()

    // Background Box with scrolling content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Enable vertical scrolling
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.nonbglogo), // Replace with your logo
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 0.dp)
            )
            // Title
            Text(
                text = "Create a new account",
                fontSize = 20.sp,
                color = customBlue,
                modifier = Modifier.padding(bottom = 10.dp),
                textAlign = TextAlign.Center
            )

            // First Name Input
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            // Last Name Input
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            // NIC Input
            OutlinedTextField(
                value = nic,
                onValueChange = { nic = it },
                label = { Text("NIC") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            // Username Input
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
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
                    .padding(bottom = 5.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            // Address Input
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            // Contact Number Input
            OutlinedTextField(
                value = contactNumber,
                onValueChange = { contactNumber = it },
                label = { Text("Contact Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // Register Button
            Button(
                onClick = {
                    val registerDto = UserRegisterDto(
                        nic = nic,
                        email = email,
                        firstName = firstName,
                        lastName = lastName,
                        username = username,
                        password = password,
                        address = address,
                        contactNumber = contactNumber
                    )
                    authViewModel.register(registerDto)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = customBlue)
            ) {
                Text("Register", fontSize = 16.sp, color = Color.Black)
            }

            // Error message
            error?.let {
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            // Navigate to Login
            Text(
                text = "Already have an account? Login here",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { navController.navigate("login") },
                textAlign = TextAlign.Center
            )
        }
    }
}