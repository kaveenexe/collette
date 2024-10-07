package com.example.colllette.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colllette.model.CartItem
import com.example.colllette.ui.theme.customBlue
import com.example.colllette.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    productViewModel: ProductViewModel,
    onNavigateBack: () -> Unit
) {
    val cart by productViewModel.cart.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    val error by productViewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (error != null) {
                Text(text = error ?: "An unknown error occurred", color = MaterialTheme.colorScheme.error)
            } else if (cart?.items?.isEmpty() == true) {
                Text(
                    "Your cart is empty",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineSmall
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cart?.items ?: emptyList()) { item ->
                        CartItemCard(
                            item = item,
                            onQuantityIncrease = { productViewModel.updateCartItemQuantity(item.productId, item.quantity + 1) },
                            onQuantityDecrease = { productViewModel.updateCartItemQuantity(item.productId, item.quantity - 1) },
                            onRemove = { productViewModel.removeFromCart(item.productId) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Total: $${String.format("%.2f", cart?.totalPrice ?: 0.0)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { productViewModel.clearCart() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = customBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Clear Cart", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.productName, fontWeight = FontWeight.Bold)
                Text(text = "$${item.price}", color = MaterialTheme.colorScheme.secondary)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onQuantityDecrease, enabled = item.quantity > 1) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease quantity")
                }
                Text(text = item.quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = onQuantityIncrease) {
                    Icon(Icons.Default.Add, contentDescription = "Increase quantity")
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove item", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}