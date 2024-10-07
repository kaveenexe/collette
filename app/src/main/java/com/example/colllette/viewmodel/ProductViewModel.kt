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

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _cart = MutableStateFlow(Cart("", "user123"))
    val cart: StateFlow<Cart> = _cart

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchProducts()
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

    fun addToCart(product: Product) {
        _cart.update { currentCart ->
            val existingItem = currentCart.items.find { it.productId == product.id }
            if (existingItem != null) {
                existingItem.quantity++
            } else {
                currentCart.items.add(CartItem(product.id, product.name, 1, product.price))
            }
            currentCart.copy(totalPrice = calculateTotalPrice(currentCart.items))
        }
    }

    fun removeFromCart(productId: String) {
        _cart.update { currentCart ->
            val updatedItems = currentCart.items.filter { it.productId != productId }
            currentCart.copy(
                items = updatedItems.toMutableList(),
                totalPrice = calculateTotalPrice(updatedItems)
            )
        }
    }

    fun updateItemQuantity(productId: String, newQuantity: Int) {
        _cart.update { currentCart ->
            val updatedItems = currentCart.items.map { item ->
                if (item.productId == productId) item.copy(quantity = newQuantity) else item
            }
            currentCart.copy(
                items = updatedItems.toMutableList(),
                totalPrice = calculateTotalPrice(updatedItems)
            )
        }
    }

    private fun calculateTotalPrice(items: List<CartItem>): Double {
        return items.sumOf { it.price * it.quantity }
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