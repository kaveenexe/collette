package com.example.colllette.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colllette.network.UserRegisterDto
import com.example.colllette.viewmodel.AuthViewModel

@Composable
fun RegistrationScreen(authViewModel: AuthViewModel = viewModel()) {
    var nic by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    val error by authViewModel.error.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        // Add TextFields for each field
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        // ... Add other fields similarly ...

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
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Register")
        }
        error?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}