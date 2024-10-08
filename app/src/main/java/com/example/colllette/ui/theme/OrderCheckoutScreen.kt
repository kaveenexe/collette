package com.example.colllette.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colllette.R
import com.example.colllette.ui.theme.customBlue
import com.example.colllette.ui.theme.darkBlue

@Composable
fun OrderCheckoutScreen(navController: NavController) {
    val context = LocalContext.current

    // Constants for styling
    val cardElevation = 8.dp
    val spacing = 16.dp
    val textSizeLarge = 22.sp
    val textSizeMedium = 20.sp
    val textSizeSmall = 18.sp

    // Sample order items with images from the drawable folder
    val orderItems = listOf(
        OrderItem("Blue Frock", 1, 5000, R.drawable.frock), // Use your drawable resource
        OrderItem("Denim Jeans", 2, 7900, R.drawable.jean) // Use your drawable resource
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
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                        }
                        Text(
                            text = "Checkout",
                            fontSize = textSizeLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Tabs (Delivery and Payment)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth().padding(horizontal = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TabWithIcon(
                            text = "Delivery",
                            isSelected = true,
                            onClick = { /* Handle Delivery Tab */ }
                        )
                        TabWithIcon(
                            text = "Payment",
                            isSelected = false,
                            onClick = { /* Handle Payment Tab */ }
                        )
                    }
                }
            }

            // Main content below the top bar
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(18.dp))

                // Delivery Address section
                AddressCard()

                // Order Summary section
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = spacing)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Order Summary",
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

                // Order Items in a card
                Spacer(modifier = Modifier.height(16.dp))
                val totalAmount = OrderItemsCard(orderItems)

                // Total Amount and Confirm Button
                Spacer(modifier = Modifier.height(32.dp))
                TotalAndConfirmRow(totalAmount)
            }
        }
    }
}

data class OrderItem(
    val name: String,      // Name of the item
    val quantity: Int,     // Quantity of the item
    val price: Int,        // Price of the item
    val imageRes: Int      // Drawable resource ID for the item's image
)

@Composable
fun OrderItemsCard(orderItems: List<OrderItem>): Int {
    var totalAmount = 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 1.dp)
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display clothing items
            for (item in orderItems) {
                ClothingItemRow(
                    itemName = item.name,
                    quantity = item.quantity,
                    price = item.price,
                    imageRes = item.imageRes // Use the image resource from the OrderItem
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(thickness = 1.dp) // Add a divider line after each item
                Spacer(modifier = Modifier.height(8.dp))
                totalAmount += item.price * item.quantity // Calculate total amount
            }
        }
    }
    return totalAmount // Return the total amount
}

@Composable
fun ClothingItemRow(itemName: String, quantity: Int, price: Int, imageRes: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Load image from resources
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp) // Set the desired image size
                .clip(RoundedCornerShape(8.dp)) // Optional: Add rounding to the image
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = itemName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp // Set the desired font size for the item name
            )
            Spacer(modifier = Modifier.height(4.dp)) // Add space between quantity and price
            Text(
                text = "Rs.$price",
                fontSize = 14.sp // Set the desired font size for the price
            )
            Spacer(modifier = Modifier.height(6.dp)) // Add space between item name and quantity
            Text(
                text = "Quantity: $quantity",
                fontSize = 14.sp // Set the desired font size for the quantity
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TotalAndConfirmRow(totalAmount: Int) {
    Spacer(modifier = Modifier.height(18.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp), // Add padding around the card
        colors = CardDefaults.cardColors(containerColor = Color.White) // Set card color to white
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp), // Add padding inside the card
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "TOTAL", // Display "TOTAL" label
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Rs. $totalAmount", // Display calculated total price
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(20.dp)) // Optional: add some space between the total and button

            Button(
                onClick = { /* Handle Confirm Action */ },
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Adjust the button width as needed
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp) // Rounded corners for the button
            ) {
                Text("PLACE ORDER", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun AddressCard() {
    // Delivery Address Title
    val spacing = 16.dp
    val textSizeMedium = 20.sp
    Text(
        text = "Shipping Address",
        fontWeight = FontWeight.Bold,
        fontSize = textSizeMedium,
        modifier = Modifier.padding(horizontal = spacing)
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
    ) {
        // Plus Button
        IconButton(
            onClick = { /* Handle Plus Button Click */ },
            modifier = Modifier
                .size(50.dp) // Adjust the size as needed
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray) // Optional: Add background color
        ) {
            Icon(
                imageVector = Icons.Filled.Add, // Use an appropriate icon
                contentDescription = "Add Address",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(5.dp))

        // Address Card
        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {

            Spacer(modifier = Modifier.height(4.dp)) // Space between title and card

            Card(
                modifier = Modifier
                    .fillMaxWidth() // Make the card take full width
                    .clip(RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Column {
                        // Address Lines
                        Text(
                            text = "John Doe",
                            fontSize = 18.sp, // Address line 1 font size
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "123, Main Street",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Colombo 07",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Sri Lanka",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TabWithIcon(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) darkBlue else Color.Gray
    val weight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    val fontSize = 18.sp // Set font size for text
    val iconSize = 28.dp // Set size for icon

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        // Round icon for tick
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = "$text Icon",
            tint = color,
            modifier = Modifier.size(iconSize) // Adjust icon size here
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontWeight = weight,
            color = color,
            fontSize = fontSize // Adjust text font size here
        )
    }
}