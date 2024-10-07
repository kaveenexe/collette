package com.example.colllette.ui.theme

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colllette.viewmodel.HomeViewModel
import com.example.colllette.viewmodel.HomeViewModelFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(context.applicationContext as Application)
    )

    val userState = homeViewModel.user.collectAsState()
    val user = userState.value

    Column(
        modifier = Modifier
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