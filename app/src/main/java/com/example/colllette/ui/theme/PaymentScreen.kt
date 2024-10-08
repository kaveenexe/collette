package com.example.colllette.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colllette.R
import com.example.colllette.ui.theme.darkBlue

@Composable
fun PaymentScreen(navController: NavController) {
    val cardElevation = 8.dp
    val spacing = 16.dp
    val textSizeLarge = 22.sp
    val textSizeMedium = 20.sp

    // State to track the selected payment method
    var selectedPaymentMethod by remember { mutableStateOf("Visa Card") }

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
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TabWithIcon(
                            text = "Delivery",
                            isSelected = false,
                            onClick = { /* Handle Delivery Tab */ }
                        )
                        TabWithIcon(
                            text = "Payment",
                            isSelected = true,
                            onClick = { /* Handle Payment Tab */ }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacing))

            // Payment Method Selection with images
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Payment Method",
                    fontSize = textSizeMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                PaymentOption("Visa Card", R.drawable.visa, selectedPaymentMethod == "Visa Card") {
                    selectedPaymentMethod = "Visa Card"
                }

                PaymentOption("MasterCard", R.drawable.master, selectedPaymentMethod == "MasterCard") {
                    selectedPaymentMethod = "MasterCard"
                }

                PaymentOption("Cash on Delivery", R.drawable.cod, selectedPaymentMethod == "Cash on Delivery") {
                    selectedPaymentMethod = "Cash on Delivery"
                }
            }

            Spacer(modifier = Modifier.height(spacing))

            // Total Amount and Proceed Button
            TotalAndProceedSection(navController)
        }
    }
}

@Composable
fun PaymentOption(
    optionName: String,
    iconResId: Int,
    isSelected: Boolean,
    onOptionSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onOptionSelected() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) darkBlue else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Use this for elevation in Material 3
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = optionName,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = optionName,
                fontSize = 18.sp,
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            RadioButton(
                selected = isSelected,
                onClick = onOptionSelected,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.White,
                    unselectedColor = darkBlue
                )
            )
        }
    }
}

@Composable
fun TotalAndProceedSection(navController: NavController) {
    Spacer(modifier = Modifier.height(45.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

        // Row for total amount
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Amount",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Rs. 20800",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Proceed Button
        Button(
            onClick = { /* Handle Proceed */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04167C))
        ) {
            Text(text = "PROCEED", color = Color.White, fontSize = 20.sp)
        }
    }
}
