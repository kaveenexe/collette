package com.example.colllette

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.colllette.network.ApiClient
import com.example.colllette.ui.theme.ActivationPendingScreen
import com.example.colllette.ui.RegistrationScreen
import com.example.colllette.ui.CartScreen
import com.example.colllette.ui.LoginScreen
import com.example.colllette.ui.OrderCheckoutScreen
import com.example.colllette.ui.OrderHistoryScreen
import com.example.colllette.ui.OrderSuccessScreen
import com.example.colllette.ui.PaymentScreen
import com.example.colllette.ui.ViewOrderScreen
import com.example.colllette.ui.ProductDetailsScreen
import com.example.colllette.ui.ProductListingScreen
import com.example.colllette.ui.ProfileScreen
import com.example.colllette.ui.theme.CollletteTheme
import com.example.colllette.viewmodel.ProductViewModel
import com.example.colllette.viewmodel.ProductViewModelFactory
import com.example.colllette.viewmodel.UserViewModel
import com.example.colllette.viewmodel.UserViewModelFactory
import com.example.colllette.viewmodel.OrderViewModel
import com.example.colllette.viewmodel.OrderViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollletteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CollletteApp()
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CollletteApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Initialize UserViewModel
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(application)
    )
    // Initialize ProductViewModel
    val productViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(
            application = context.applicationContext as Application,
            userViewModel = userViewModel
        )
    )
    // Initialize OrderViewModel
    val orderViewModel: OrderViewModel = viewModel(
        factory = OrderViewModelFactory(
            application = context.applicationContext as Application,
            userViewModel = userViewModel
        )
    )
    // Initialize ApiClient
    val apiClient = ApiClient(context.applicationContext)

    NavHost(navController = navController, startDestination = "home") {
        composable("login") { LoginScreen(navController) }
        composable("activationPending") { ActivationPendingScreen(navController) }
        composable("registration") { RegistrationScreen(navController) }
        composable("home") { ProductListingScreen(
            productViewModel = productViewModel,
            onNavigateToCart = { navController.navigate("cart") },
            onNavigateToProductDetails = { productId ->
                navController.navigate("productDetails/$productId")
            },
            onNavigateToProfile = { navController.navigate("profile") }, // Navigate to ProfileScreen
            onNavigateToMyOrders = { customerId ->
                navController.navigate("history/$customerId")
            }
        ) }
        composable("cart") {
            CartScreen(
                productViewModel = productViewModel,
                navController = navController,
                onNavigateBack = { navController.popBackStack() },
                onProceedToCheckout = { cart ->
                    navController.navigate("checkout/${cart.id}")
                }
            )
        }
        composable("productDetails/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            productId?.let { productViewModel.fetchProductDetails(it) }
            ProductDetailsScreen(
                productViewModel = productViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("profile") {
            ProfileScreen(navController, userViewModel = userViewModel)
        }
        composable("checkout/{cartId}") { backStackEntry ->
            val cartId = backStackEntry.arguments?.getString("cartId") ?: return@composable
            LaunchedEffect(cartId) {
                productViewModel.fetchCartByCartId(cartId)
            }
            val cart by productViewModel.cart.collectAsState()
            OrderCheckoutScreen(
                navController = navController,
                userViewModel = userViewModel,
                productViewModel = productViewModel,
                onNavigateBack = { navController.popBackStack() },
                cart = cart
            )
        }
        composable("payment") {
            PaymentScreen(
                navController = navController,
                userViewModel = userViewModel,
                productViewModel = productViewModel,
                orderViewModel = orderViewModel
            )
        }
        composable("order_success_screen/{id}/{orderId}/{customerId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val orderId = backStackEntry.arguments?.getString("orderId")
            val customerId = backStackEntry.arguments?.getString("customerId")
            OrderSuccessScreen(navController, id, orderId, customerId)
        }
        composable("view_order_screen/{customerId}/{id}") { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: return@composable
            val orderId = backStackEntry.arguments?.getString("id") ?: return@composable
            LaunchedEffect(orderId, customerId) {
                orderViewModel.getOrderByCustomerIdAndOrderId(customerId, orderId)
            }
            val order by orderViewModel.order.collectAsState()
            val orderIdWithHash = order?.orderId ?: ""
            ViewOrderScreen(
                navController = navController,
                orderId = orderIdWithHash,
                customerId = customerId,
                orderViewModel = orderViewModel,
                productViewModel = productViewModel,
                order = order
            )
        }
        composable("history/{customerId}") { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""
            OrderHistoryScreen(
                navController = navController,
                orderViewModel = orderViewModel,
                customerId = customerId
            )
        }
    }
}