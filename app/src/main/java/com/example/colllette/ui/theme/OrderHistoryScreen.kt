package com.example.colllette.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.colllette.ui.theme.darkBlue
import com.example.colllette.viewmodel.OrderViewModel
import com.example.colllette.model.Order
import com.example.colllette.model.OrderStatus

@Composable
fun OrderHistoryScreen(navController: NavController, orderViewModel: OrderViewModel, customerId: String) {
    LaunchedEffect(Unit) {
        orderViewModel.fetchOrdersByCustomerId(customerId)
    }
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
            } else if (orders.isEmpty()) {
                // Display a message if there are no orders
                Text(
                    text = "No orders found.",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                // Display the list of orders
                LazyColumn {
                    items(orders.size) { index ->
                        val order = orders[index]  // Get the order at the current index
                        OrderCard(order = order, cardElevation = cardElevation, navController = navController)  // Pass the order to the OrderCard
                    }
                }
            }
        }
    }
}

fun getOrderStatusDetails(status: Int): Pair<String, Color> {
    return when (OrderStatus.fromStatusValue(status)) {
        OrderStatus.Purchased -> "Purchased" to Color(0xFF8FB8D5) // Replace with your desired color
        OrderStatus.Accepted -> "Accepted" to Color(0xFFB7E8A0)
        OrderStatus.Processing -> "Processing" to Color(0xFFD1A6E3)
        OrderStatus.PartiallyDelivered -> "Partially Delivered" to Color(0xFFDBA695)
        OrderStatus.Delivered -> "Delivered" to Color(0xFFE0C66A)
        OrderStatus.Cancelled -> "Cancelled" to Color(0xFFE0A3A3)
        OrderStatus.Pending -> "Pending" to Color(0xFFB0C8E0)
    }
}

@Composable
fun OrderCard(order: Order, cardElevation: Dp, navController: NavController) {
    // Get the status details using the helper function
    val (orderStatusString, statusColor) = getOrderStatusDetails(order.status.toInt())

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
                    // Order ID
                    Text(
                        text = "Order ID - ${order.orderId}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                val formattedOrderDate = formatOrderDate(order.orderDate)

                Text(
                    text = formattedOrderDate,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

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
                    text = orderStatusString,
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
                        navController.navigate("view_order_screen/${order.customerId}/${order.id}")
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