package com.example.colllette.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.colllette.ui.theme.darkBlue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.colllette.R

@Composable
fun ViewOrderScreen(navController: NavController) {
    val cardElevation = 8.dp
    val spacing = 16.dp
    val textSizeLarge = 22.sp
    val textSizeMedium = 20.sp

    var showDialog by remember { mutableStateOf(false) }

    // Define your statuses with custom colors
    val statuses = listOf(
        "Purchased" to Color(0xFF8FB8D5),
        "Accepted" to Color(0xFFB7E8A0),
        "Processing" to Color(0xFFD1A6E3),
        "Delivered" to Color(0xFFE0C66A),
        "Cancelled" to Color(0xFFE0A3A3),
        "Pending" to Color(0xFFB0C8E0)
    )

    // Sample order items
    val orderItems = listOf(
        OrderItemData("Blue Frock", "Rs.5000.00", "Size: M, Color: Black", 0, R.drawable.frock), // Use your drawable resource
        OrderItemData("Denim Jeans", "RS.7900.00", "Size: L, Color: Light Blue", 2, R.drawable.jean) // Use your drawable resource
    )

    // Background with light ash color
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3)) // Light ash color
    ) {
        Column {
            // Top Bar with back button and title
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(cardElevation)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing)
                ) {
                    // Row for back button and title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                        }
                        Text(
                            text = "Order",
                            fontSize = textSizeLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Order Status Row
            OrderStatusRow()

            Spacer(modifier = Modifier.height(25.dp))

            // Order ID and Date Card
            Card(
                modifier = Modifier
                    .padding(horizontal = spacing)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(cardElevation),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp) // Adjust padding here if needed
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, // Distributes space between elements
                        verticalAlignment = Alignment.CenterVertically // Aligns elements vertically in the center
                    ) {
                        // Order ID
                        Text(
                            text = "Order ID #ORD78968", // Replace with dynamic order ID
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )

                        // Order Date
                        Text(
                            text = "October 07, 2024", // Replace with dynamic order date
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    // Optional Spacer to reduce space between Row and Column
                    Spacer(modifier = Modifier.height(8.dp)) // Adjust height as needed

                    // Column for additional information
                    Column(
                        modifier = Modifier.padding(top = 8.dp) // Adjust top padding here if needed
                    ) {
                        // Total Amount
                        Text(
                            text = "Order Amount Rs.20800.00", // Replace with dynamic total amount
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Estimated Delivery Date
                        Text(
                            text = "Estimated Delivery on October 15, 2024", // Replace with dynamic date
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = spacing)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order Items",
                    fontWeight = FontWeight.Bold,
                    fontSize = textSizeMedium,
                    modifier = Modifier.weight(1f) // Makes sure the text takes available space
                )

                // Circle to show the number of items
                Box(
                    modifier = Modifier
                        .size(30.dp) // Size of the circle
                        .background(darkBlue, shape = CircleShape) // Circle shape with a background color
                        .border(1.dp, Color.White, shape = CircleShape), // Optional white border
                    contentAlignment = Alignment.Center // Centers the text inside the circle
                ) {
                    Text(
                        text = "2", // Replace with the actual item count
                        color = Color.White, // Text color
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp // Adjust the font size as needed
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // List of Items
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                orderItems.forEach { item ->
                    // Access the corresponding status using item.status
                    val statusIndex = item.status // Assuming item.status is an Int representing the index
                    val statusPair = statuses.getOrNull(statusIndex) // Use getOrNull to avoid out of bounds
                    val statusName = statusPair?.first ?: "Unknown" // Get the status name
                    val statusColor = statusPair?.second ?: Color.Gray // Get the status color

                    OrderItem(
                        name = item.name,
                        price = item.price,
                        details = item.details,
                        status = statusName,
                        statusColor = statusColor,
                        imageRes = item.imageRes
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Cancel and Message buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Cancel Button with border and square shape
                OutlinedButton(
                    onClick = { showDialog = true }, // Show the confirmation dialog on click
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .width(170.dp)
                        .height(50.dp),
                    border = BorderStroke(2.dp, darkBlue)
                ) {
                    Text(text = "Cancel Order", color = darkBlue, fontSize = 18.sp)
                }

                // Message Button with blue background and white text
                Button(
                    onClick = { /* TODO: Send Message */ },
                    colors = ButtonDefaults.buttonColors(containerColor = darkBlue),
                    shape = RoundedCornerShape(6.dp), // Slightly rounded corners
                    modifier = Modifier
                        .width(170.dp) // Same width as Cancel button for consistency
                        .height(50.dp) // Same height for square shape
                ) {
                    Text(text = "Message", color = Color.White, fontSize = 18.sp)
                }
            }
        }

        // Show confirmation dialog if showDialog is true
        if (showDialog) {
            ConfirmationDialog(
                onConfirm = {
                    showDialog = false
                    // TODO: Perform order cancellation here
                },
                onDismiss = {
                    showDialog = false // Just close the dialog if canceled
                }
            )
        }
    }
}

@Composable
fun OrderStatusRow() {
    val statusSteps = listOf("Purchased", "Accepted", "Processing", "Delivered")
    val currentStep = 2 // This controls up to which step is completed

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        statusSteps.forEachIndexed { index, step ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Circle for each status step
                Box(
                    modifier = Modifier
                        .size(30.dp) // Adjust circle size
                        .background(
                            color = if (index <= currentStep) darkBlue else Color.Gray,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Add tick for completed steps
                    if (index <= currentStep) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp)) // Reduced space between circle and line

                // Status Label
                Text(
                    text = step,
                    fontSize = 14.sp,
                    fontWeight = if (index == currentStep) FontWeight.Bold else FontWeight.Normal,
                    color = if (index <= currentStep) Color.Black else Color.Gray
                )
            }

            // Connecting line between steps, except for the last one
            if (index < statusSteps.size - 1) {
                Box(
                    modifier = Modifier
                        .offset(y = (-12).dp) // Move the line closer to the circle by reducing this value
                ) {
                    Canvas(
                        modifier = Modifier
                            .height(3.dp)
                            .width(30.dp), // Adjust this width for line length
                    ) {
                        drawLine(
                            color = if (index < currentStep) darkBlue else Color.Gray,
                            start = Offset(0f, size.height / 2),
                            end = Offset(size.width, size.height / 2),
                            strokeWidth = 10f // Adjust thickness of the line
                        )
                    }
                }
            }
        }
    }
}

data class OrderItemData(val name: String, val price: String, val details: String, val status: Int, val imageRes: Int)

@Composable
fun OrderItem(name: String, price: String, details: String, status: String, statusColor: Color, imageRes: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Item Image
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.size(70.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Item Details
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = details, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = price, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            }

            // Status Badge
            Text(
                text = status,
                color = Color.White,
                modifier = Modifier
                    .background(statusColor, shape = RoundedCornerShape(4.dp)) // Added rounded corners with 4.dp radius
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Custom confirmation dialog
@Composable
fun ConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure you want to cancel the order?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Cancel Button in dialog
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        shape = RoundedCornerShape(6.dp), // Slightly rounded corners
                        modifier = Modifier
                            .width(107.dp)
                            .height(35.dp)
                    ) {
                        Text(text = "CANCEL", color = Color.Black)
                    }

                    // Confirm Button in dialog
                    Button(
                        onClick = { onConfirm() },
                        colors = ButtonDefaults.buttonColors(containerColor = darkBlue),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .width(100.dp)
                            .height(35.dp)
                    ) {
                        Text(text = "OK", color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderScreen() {
    val mockNavController = rememberNavController() // Use a remembered NavController in the preview
    ViewOrderScreen(navController = mockNavController)
}