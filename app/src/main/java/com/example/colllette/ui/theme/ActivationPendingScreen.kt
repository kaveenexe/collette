package com.example.colllette.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ActivationPendingScreen(navController: NavController) {
    // Styled container for activation message and button
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Activation message
            Text(
                text = "Your account is pending activation. Please wait for an administrator to activate your account.",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            // Go to login again button
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = customBlue)
            ) {
                Text("Go to Login", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
