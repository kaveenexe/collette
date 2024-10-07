package com.example.colllette.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.colllette.model.Product
import com.example.colllette.model.Cart
import com.example.colllette.model.CartItem
import com.example.colllette.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val apiClient = ApiClient(application)
    private val userId = "user123" // Replace this with actual user ID when available

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _cart = MutableStateFlow<Cart?>(null)
    val cart: StateFlow<Cart?> = _cart

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchProducts()
        fetchCart()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedProducts = apiClient.productApi.getProducts()
                _products.value = fetchedProducts
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to fetch products: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchCart() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedCart = apiClient.productApi.getCart(userId)
                _cart.value = fetchedCart
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to fetch cart: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                val cartItem = CartItem(product.id, product.name, 1, product.price)
                apiClient.productApi.addToCart(userId, cartItem)
                fetchCart() // Refresh the cart after adding an item
            } catch (e: Exception) {
                _error.value = "Failed to add item to cart: ${e.message}"
            }
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            try {
                apiClient.productApi.removeFromCart(userId, productId)
                fetchCart() // Refresh the cart after removing an item
            } catch (e: Exception) {
                _error.value = "Failed to remove item from cart: ${e.message}"
            }
        }
    }

    fun updateCartItemQuantity(productId: String, newQuantity: Int) {
        viewModelScope.launch {
            try {
                val currentCart = _cart.value
                val item = currentCart?.items?.find { it.productId == productId }
                if (item != null) {
                    val updatedItem = item.copy(quantity = newQuantity)
                    apiClient.productApi.updateCartItem(userId, productId, updatedItem)
                    fetchCart() // Refresh the cart after updating an item
                }
            } catch (e: Exception) {
                _error.value = "Failed to update item quantity: ${e.message}"
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            try {
                apiClient.productApi.deleteCart(userId)
                _cart.value = null
            } catch (e: Exception) {
                _error.value = "Failed to clear cart: ${e.message}"
            }
        }
    }
}

class ProductViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}