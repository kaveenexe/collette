package com.example.colllette.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colllette.R

@Composable
fun OrderSuccessScreen(navController: NavController, id: String?, orderId: String?, customerId: String?) {
    // Background color for the success screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3)), // Background blue color
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Checkmark icon (replace with your own icon if necessary)
            SuccessIcon()

            Spacer(modifier = Modifier.height(40.dp))

            // "Order Placed" text
            Text(
                text = "Order Placed!",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Order ID: ${orderId ?: "Unknown"}", // Display the order ID
                color = Color.White,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(35.dp))

            // Description text
            Text(
                text = "Your order was placed successfully.\n" +
                        "For more details, check Delivery Status\n" +
                        "under Profile tab.",
                color = Color.White,
                fontSize = 18.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // "Delivery Status" button
            Button(
                onClick = {
                    navController.navigate("view_order_screen/${customerId}/${id}")
                },
                modifier = Modifier
                    .width(250.dp)
                    .padding(horizontal = 32.dp)
                    .height(45.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Delivery Status",
                    color = Color(0xFF2196F3),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SuccessIcon() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder for a checkmark icon, replace with a real icon
        val painter: Painter = painterResource(id = R.drawable.checked) // Use your check icon
        Image(painter = painter, contentDescription = "Success")
    }
}
