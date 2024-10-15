package com.example.colllette.ui

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.colllette.data.local.UserEntity
import com.example.colllette.model.BillingDetails
import com.example.colllette.model.Cart
import com.example.colllette.model.Order
import com.example.colllette.model.OrderItemGroup
import com.example.colllette.ui.theme.darkBlue
import com.example.colllette.viewmodel.OrderViewModel
import com.example.colllette.viewmodel.ProductViewModel
import com.example.colllette.viewmodel.UserViewModel
import java.time.LocalDateTime

@Composable
fun PaymentScreen(navController: NavController, userViewModel: UserViewModel, productViewModel: ProductViewModel, orderViewModel: OrderViewModel) {
    val cardElevation = 8.dp
    val spacing = 16.dp
    val textSizeLarge = 22.sp
    val textSizeMedium = 20.sp

    // State to track the selected payment method
    var selectedPaymentMethod by remember { mutableStateOf("Visa Card") }

    // Get user and cart details
    val user by userViewModel.user.collectAsState()
    val cart by productViewModel.cart.collectAsState()

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
            cart?.let {
                TotalAndProceedSection(navController, selectedPaymentMethod,
                    it, user, orderViewModel)
            }
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
fun TotalAndProceedSection(navController: NavController, paymentMethod: String, cart: Cart, user: UserEntity?, orderViewModel: OrderViewModel) {
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
                text = "Rs. ${cart.totalPrice}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Proceed Button
        @RequiresApi(Build.VERSION_CODES.O)
        @Composable
        fun TotalAndProceedSection(navController: NavController, paymentMethod: String, cart: Cart, user: UserEntity?, orderViewModel: OrderViewModel) {
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
                        text = "Rs. ${cart.totalPrice}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                // Proceed Button
                Button(
                    onClick = {
                        // Fetch user details from userViewModel
                        val billingDetails = user?.let {
                            BillingDetails(
                                customerName = "${it.firstName} ${it.lastName}",
                                email = it.email ?: "",  // Assuming email is always present
                                phone = it.contactNumber, // Nullable phone number
                                singleBillingAddress = it.address ?: "", // Assuming address is provided
                                billingAddress = null // Pass billingAddress as null correctly
                            )
                        }

                        // Map cart items to OrderItem
                        val orderItemsGroups = listOf(
                            OrderItemGroup(
                                listItemId = 1, // Example, adjust as necessary
                                items = cart.items.map { cartItem ->
                                    com.example.colllette.model.OrderItem(
                                        productId = cartItem.productId,
                                        productName = cartItem.productName,
                                        vendorId = "someVendorId",
                                        quantity = cartItem.quantity,
                                        price = cartItem.price,
                                        productStatus = 0
                                    )
                                }
                            )
                        )

                        // Create Order object
                        val order = Order(
                            id = "", // Auto-generated by backend
                            orderId = "", // Auto-generated by backend
                            status = "Purchased", // Initial order status
                            orderDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(System.currentTimeMillis())),
                            paymentMethod = paymentMethod, // Use selected payment method
                            orderItemsGroups = orderItemsGroups,
                            totalAmount = cart.totalPrice,
                            customerId = user?.id, // Assuming user object is non-null
                            createdByCustomer = true,
                            billingDetails = billingDetails // Pass the billing details
                        )

                        // Call createOrder method in the ViewModel and navigate on success
                        orderViewModel.createOrder(order) { createdOrderId ->
                            // Navigate to OrderSuccessScreen with the order ID
                            navController.navigate("order_success_screen/$createdOrderId")
                        }
                    },
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
    }
}
