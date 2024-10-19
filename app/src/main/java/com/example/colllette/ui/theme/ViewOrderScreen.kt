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
import androidx.navigation.NavController
import com.example.colllette.ui.theme.darkBlue
import androidx.compose.runtime.remember
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import coil.compose.rememberAsyncImagePainter
import com.example.colllette.model.Order
import com.example.colllette.model.OrderStatus
import com.example.colllette.network.CancelRequestStatus
import com.example.colllette.network.OrderCancellation
import com.example.colllette.viewmodel.OrderViewModel
import com.example.colllette.viewmodel.ProductViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun ViewOrderScreen(
    navController: NavController,
    orderId: String,
    customerId: String,
    orderViewModel: OrderViewModel,
    productViewModel: ProductViewModel,
    order: Order?
) {
    val products by productViewModel.products.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val error by orderViewModel.error.collectAsState()

    val cardElevation = 8.dp
    val spacing = 16.dp
    val textSizeLarge = 22.sp
    val textSizeMedium = 20.sp

    var showDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Define your statuses with custom colors
    val statuses = listOf(
        "Purchased" to Color(0xFF8FB8D5),
        "Accepted" to Color(0xFFB7E8A0),
        "Processing" to Color(0xFFD1A6E3),
        "Delivered" to Color(0xFFE0C66A),
        "PartiallyDelivered" to Color(0xFFEEB9A8),
        "Cancelled" to Color(0xFFD17D7D),
        "Pending" to Color(0xFFB0C8E0)
    )

    // Get the current status of the order and map to index
    val currentStatusStep = getStatusStepIndex(order?.status?.toInt() ?: 0)

    // Background with light ash color
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3)) // Light ash color
    ) {
        if (isLoading) {
            // Show a loading indicator
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (error != null) {
            // Show an error message
            Text("Error: $error", color = Color.Red, modifier = Modifier.align(Alignment.Center))
        } else {
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
                OrderStatusRow(currentStep = currentStatusStep)

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
                                text = "Order ID ${order?.orderId ?: "N/A"}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )

                            // Use the function in your Text composable
                            val formattedOrderDate = formatOrderDate(order?.orderDate)

                            // Display the formatted date
                            Text(
                                text = formattedOrderDate,
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
                                text = "Order Amount Rs.${order?.totalAmount ?: "N/A"}",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            val estimatedDeliveryDate = calculateEstimatedDelivery(order?.orderDate)

                            // Estimated Delivery Date
                            Text(
                                text = "Estimated Delivery on $estimatedDeliveryDate",
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
                            text = "${order?.orderItemsGroups?.sumOf { it.items.size } ?: 0}",
                            color = Color.White, // Text color
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp // Adjust the font size as needed
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // List of Items
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    order?.orderItemsGroups?.forEach { itemGroup ->
                        itemGroup.items.forEach { item ->
                            val statusIndex = item.productStatus // Get the index from item.productStatus
                            val statusName = statuses.getOrNull(statusIndex)?.first ?: "Unknown" // Get the status name safely
                            val statusColor = statuses.getOrNull(statusIndex)?.second ?: Color.Gray // Get the color safely
                            val product = products.find { it.uniqueProductId == item.productId }
                            OrderItem(
                                imageUrl = product?.imageUrl,
                                name = item.productName,
                                price = "Rs.${item.price}",
                                details = "Quantity: ${item.quantity}", // Assuming quantity is part of your item model
                                status = statusName,
                                statusColor = statusColor,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
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
                        onClick = {
                            navController.navigate("home") // Navigate to the HomeScreen
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = darkBlue),
                        shape = RoundedCornerShape(6.dp), // Slightly rounded corners
                        modifier = Modifier
                            .width(170.dp) // Same width as Cancel button for consistency
                            .height(50.dp) // Same height for square shape
                    ) {
                        Text(text = "Go to Home", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }

        if (showDialog) {
            ConfirmationDialog(
                onConfirm = {
                    showDialog = false
                    val orderCancellation = OrderCancellation(
                        id = order!!.id,
                        orderId = orderId,
                        cancellationApproved = false,
                        cancellationDate = null, // Not setting a date here
                        cancelRequestStatus = CancelRequestStatus.Pending.value
                    )

                    orderViewModel.requestOrderCancellation(orderCancellation)
                    showSuccessDialog = true
                },
                onDismiss = { showDialog = false }
            )
        }

        if (showSuccessDialog) {
            SuccessDialog(
                title = "Cancellation Request Successful",
                message = "Your order cancellation request has been sent. Please wait for order cancellation approval.",
                onConfirm = { showSuccessDialog = false },
                navController = navController
            )
        }
    }
}

fun getStatusStepIndex(status: Int): Int {
    return when (status) {
        0 -> 0 // Purchased
        1 -> 1 // Accepted
        2, 3, 4, 5 -> 2 // Processing, Partially Delivered, Pending, Cancelled
        6 -> 3 // Delivered
        else -> -1
    }
}

@Composable
fun OrderStatusRow(currentStep: Int) {
    val statusSteps = listOf("Purchased", "Accepted", "Processing", "Delivered")

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

@Composable
fun SuccessDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    navController: NavController
) {
    Dialog(onDismissRequest = { onConfirm() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier
                .padding(14.dp)
                .widthIn(min = 200.dp, max = 300.dp)
                .heightIn(min = 150.dp, max = 200.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = message,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = {
                        onConfirm() // Call the confirm action
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        } // Redirect to the home page
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = darkBlue)
                ) {
                    Text(text = "OK", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    message: String = "Are you sure you want to cancel the order?",
    confirmText: String = "OK",
    dismissText: String = "CANCEL",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // Use the Dialog composable provided by Compose
    Dialog(onDismissRequest = { onDismiss() }) {
        DialogContent(
            message = message,
            confirmText = confirmText,
            dismissText = dismissText,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)), // Optional: dim background
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content() // Call the content that is passed to the dialog
            }
        }
    }
}

@Composable
private fun DialogContent(
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        DialogButtons(
            confirmText = confirmText,
            dismissText = dismissText,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun DialogButtons(
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { onDismiss() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        ) {
            Text(text = dismissText, color = Color.White)
        }

        Button(
            onClick = { onConfirm() },
            colors = ButtonDefaults.buttonColors(containerColor = darkBlue),
            modifier = Modifier.weight(1f).padding(start = 8.dp)
        ) {
            Text(text = confirmText, color = Color.White)
        }
    }
}

// Function to format the estimated delivery date
fun calculateEstimatedDelivery(orderDateString: String?): String {
    return orderDateString?.let { dateString ->
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.ENGLISH)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val orderDate: Date = inputFormat.parse(dateString) ?: return "N/A"

            // Adding 7 days to the order date
            val calendar = Calendar.getInstance()
            calendar.time = orderDate
            calendar.add(Calendar.DAY_OF_MONTH, 7)

            // Format the new date
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
            outputFormat.format(calendar.time)
        } catch (e: Exception) {
            "N/A"
        }
    } ?: "N/A"
}

// Function to format the order date
fun formatOrderDate(orderDateString: String?): String {
    return orderDateString?.let { dateString ->
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.ENGLISH)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date: Date = inputFormat.parse(dateString) ?: return "N/A"
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
            outputFormat.format(date)
        } catch (e: Exception) {
            "N/A"
        }
    } ?: "N/A"
}

@Composable
fun OrderItem(imageUrl: String?, name: String, price: String, details: String, status: String, statusColor: Color) {
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
            // Display the product image if available
            imageUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                )
            }
            Spacer(modifier = Modifier.width(15.dp))

            // Item Details
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = details, color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(3.dp))
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
