package com.example.colllette.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.colllette.model.Order
import com.example.colllette.network.ApiClient
import com.example.colllette.network.OrderApi
import com.example.colllette.network.OrderCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    application: Application,
    private val userViewModel: UserViewModel // Add UserViewModel as a parameter
) : AndroidViewModel(application) {
    // Initialize the API client for accessing order-related APIs
    private val apiClient = ApiClient(application)
    private val orderApi: OrderApi = apiClient.orderApi

    // StateFlow to hold the current order being processed
    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> get() = _order

    // StateFlow to hold a list of all orders for the user
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> get() = _orders

    // StateFlow to indicate whether an order cancellation was successful
    private val _isOrderCancelled = MutableStateFlow<Boolean?>(null)
    val isOrderCancelled: StateFlow<Boolean?> get() = _isOrderCancelled

    // StateFlow to manage loading state during API calls
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // StateFlow to hold error messages
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    // Initialize by fetching the logged-in user and their orders
    init {
        viewModelScope.launch {
            userViewModel.user.collect { userEntity ->
                val userId = userEntity?.id // Get the logged-in user ID
                if (userId != null) {
                    fetchOrdersByCustomerId(userId)
                } else {
                    _error.value = "User not logged in."
                }
            }
        }
    }

    // Fetch orders by customerId
    fun fetchOrdersByCustomerId(customerId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedOrders = orderApi.getOrdersByCustomerId(customerId) // Call API
                _orders.value = fetchedOrders // Update the orders list
                _error.value = null // Clear any previous error
            } catch (e: Exception) {
                _error.value = "Failed to fetch orders: ${e.message}" // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    // New order creation
    fun createOrder(order: Order, onSuccess: (String, String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true // Set loading state to true
            try {
                val createdOrder = orderApi.createOrder(order)
                _order.value = createdOrder // Update the current order state
                _error.value = null // Clear any previous errors
                onSuccess(createdOrder.id, createdOrder.orderId) // Pass both id and orderId
            } catch (e: Exception) {
                _error.value = "Failed to create order: ${e.message}" // Set error message
            } finally {
                _isLoading.value = false // Set loading state to false
            }
        }
    }

    // Fetch a specific order by customer ID and order ID
    fun getOrderByCustomerIdAndOrderId(customerId: String, orderId: String) {
        viewModelScope.launch {
            _isLoading.value = true // Set loading state to true
            try {
                val fetchedOrder = orderApi.getOrderByCustomerIdAndOrderId(customerId, orderId) // Fetch order
                if (fetchedOrder != null) {
                    _order.value = fetchedOrder // Update the current order state
                    _error.value = null // Clear any previous errors
                } else {
                    _error.value = "Order not found." // Set error if order not found
                }
            } catch (e: Exception) {
                _error.value = "Failed to fetch order: ${e.message}" // Set error message
            } finally {
                _isLoading.value = false // Set loading state to false
            }
        }
    }

    // Fetch a specific order by order ID
    fun getOrderById(orderId: String) {
        viewModelScope.launch {
            _isLoading.value = true // Set loading state to true
            try {
                val fetchedOrder = orderApi.getOrderById(orderId) // Fetch order
                _order.value = fetchedOrder // Update the current order state
                _error.value = null // Clear any previous errors
            } catch (e: Exception) {
                _error.value = "Failed to fetch order: ${e.message}" // Set error message
            } finally {
                _isLoading.value = false // Set loading state to false
            }
        }
    }

    // Request cancellation of an order
    fun requestOrderCancellation(cancellation: OrderCancellation) {
        viewModelScope.launch {
            _isLoading.value = true // Set loading state to true
            try {
                val response = orderApi.requestOrderCancellation(cancellation) // Use the cancellation object directly
                if (response.isSuccessful) {
                    _isOrderCancelled.value = true // Set cancellation success state
                    _error.value = null // Clear any previous errors
                } else {
                    _isOrderCancelled.value = false // Set cancellation failure state
                    _error.value = "Failed to request cancellation: ${response.message()}" // Set error message
                }
            } catch (e: Exception) {
                _isOrderCancelled.value = false // Set cancellation failure state
                _error.value = "Failed to request cancellation: ${e.message}" // Set error message
            } finally {
                _isLoading.value = false // Set loading state to false
            }
        }
    }
}

class OrderViewModelFactory(
    private val application: Application,
    private val userViewModel: UserViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(application, userViewModel) as T // Pass UserViewModel to OrderViewModel
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}