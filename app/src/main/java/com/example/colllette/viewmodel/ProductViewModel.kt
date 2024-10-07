package com.example.colllette.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.colllette.model.Product
import com.example.colllette.model.Cart
import com.example.colllette.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _cart = MutableStateFlow(Cart("", "user123"))
    val cart: StateFlow<Cart> = _cart

    init {
        // Simulate fetching products
        viewModelScope.launch {
            _products.value = listOf(
                Product(
                    id = "1",
                    uniqueProductId = "T001",
                    name = "T-Shirt",
                    description = "Comfortable cotton T-Shirt",
                    category = "T-Shirts",
                    price = 19.99,
                    stockQuantity = 100,
                    vendorId = "V001",
                    isActive = true
                ),
                Product(
                    id = "2",
                    uniqueProductId = "J001",
                    name = "Jeans",
                    description = "Classic blue jeans",
                    category = "Trousers",
                    price = 49.99,
                    stockQuantity = 50,
                    vendorId = "V002",
                    isActive = true
                )
                // Add more sample products as needed
            )
        }
    }

    fun addToCart(product: Product) {
        _cart.update { currentCart ->
            val existingItem = currentCart.items.find { it.productId == product.uniqueProductId }
            if (existingItem != null) {
                existingItem.quantity++
            } else {
                currentCart.items.add(CartItem(product.uniqueProductId, product.name, 1, product.price))
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

class ProductViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}