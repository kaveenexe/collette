package com.example.colllette.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.colllette.ui.theme.darkBlue
import com.example.colllette.viewmodel.OrderViewModel

@Composable
fun OrderHistoryScreen(navController: NavController, orderViewModel: OrderViewModel) {
    // Observe orders from the ViewModel
    val orders by orderViewModel.orders.collectAsState(initial = emptyList())
    val isLoading by orderViewModel.isLoading.collectAsState(initial = false)
    val error by orderViewModel.error.collectAsState(initial = null)

    val cardElevation = 8.dp
    val spacing = 16.dp
    val textSizeLarge = 22.sp

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
                            text = "My Orders",
                            fontSize = textSizeLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            // Show loading indicator
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            // Show error message
            else if (error != null) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                // Display a message if there are no orders
                Text(
                    text = "No orders found.",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, cardElevation: Dp) {
    // Define your statuses with custom colors
    val statuses = mapOf(
        "Purchased" to Color(0xFF8FB8D5),
        "Accepted" to Color(0xFFB7E8A0),
        "Processing" to Color(0xFFD1A6E3),
        "Delivered" to Color(0xFFE0C66A),
        "Cancelled" to Color(0xFFE0A3A3),
        "Pending" to Color(0xFFB0C8E0)
    )

    // Get the color for the current order status
    val statusColor = statuses[order.status] ?: Color.Gray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),  // Reduce padding to 8.dp or lower
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(cardElevation),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Order ID and Date
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Dashed-line square for icon
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(order.iconColor ?: Color.Gray, order.iconColor ?: Color.Gray)
                                ),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (order.customIconRes != null) {
                            Image(
                                painter = painterResource(id = order.customIconRes),
                                contentDescription = "Order Status Custom Icon",
                                modifier = Modifier.size(40.dp)
                            )
                        } else {
                            Icon(
                                imageVector = order.icon ?: Icons.Default.Info,
                                contentDescription = "Order Status Icon",
                                tint = order.iconColor ?: Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(15.dp))

                    // Order ID
                    Text(
                        text = "Order ID - ${order.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = order.date,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Amount",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "Rs.${order.totalAmount}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Status with Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                // Status Badge
                Text(
                    text = order.status,
                    color = Color.White,
                    modifier = Modifier
                        .background(statusColor, shape = RoundedCornerShape(4.dp)) // Rounded corners for the badge
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Track Order and Report Issue buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Report Issue Button
                OutlinedButton(
                    onClick = {
                        // TODO: Implement report issue functionality
                        // e.g., navController.navigate("report_issue/${order.id}")
                    },
                    modifier = Modifier
                        .width(155.dp)  // Custom width
                        .height(35.dp),  // Custom height
                    shape = RoundedCornerShape(8.dp),  // Small border radius
                    border = BorderStroke(2.dp, darkBlue),  // Border color and stroke width
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = darkBlue  // Text color
                    )
                ) {
                    Text(text = "Report Issue", color = darkBlue, fontSize = 15.sp)
                }

                // Track Order Button
                Button(
                    onClick = {
                        // TODO: Implement track order functionality
                        // e.g., navController.navigate("view_order_screen/{orderId}/{customerId}")
                    },
                    modifier = Modifier
                        .width(155.dp)  // Custom width
                        .height(35.dp),  // Custom height
                    shape = RoundedCornerShape(8.dp),  // Small border radius
                    colors = ButtonDefaults.buttonColors(
                        containerColor = darkBlue,  // Button background color
                        contentColor = Color.White  // Text color
                    )
                ) {
                    Text(text = "Track Order", color = Color.White, fontSize = 15.sp)
                }
            }
        }
    }
}

data class Order(
    val id: String,
    val date: String,
    val status: String,
    val icon: ImageVector? = null,
    val iconColor: Color? = null,
    val customIconRes: Int? = null,
    val customerId: String? = null,
    val orderItems: List<Unit> = emptyList(),
    val totalAmount: Double
)