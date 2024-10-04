package com.example.colllette.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Import viewModel and observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun UserScreen(userViewModel: UserViewModel = viewModel()) {
    val users by userViewModel.users.observeAsState(emptyList())
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                userViewModel.addUser(firstName, lastName)
                firstName = ""
                lastName = ""
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add User")
        }
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Text("User List:")
        LazyColumn {
            items(users.size) { index ->
                val user = users[index]
                Text("${user.uid}: ${user.firstName} ${user.lastName}")
            }
        }
    }
}
