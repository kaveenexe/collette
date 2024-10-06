package com.example.colllette.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.colllette.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        // This is a placeholder. Replace with actual API call
        val dummyProducts = listOf(
            Product(
                id = "1",
                uniqueProductId = "PROD001",
                name = "T-Shirt",
                description = "A comfortable cotton T-shirt",
                price = 19.99,
                stockQuantity = 100,
                vendorId = "VENDOR001",
                isActive = true,
            ),
            Product(
                id = "2",
                uniqueProductId = "PROD002",
                name = "Jeans",
                description = "Classic blue jeans",
                price = 49.99,
                stockQuantity = 50,
                vendorId = "VENDOR001",
                isActive = true,
            ),
            // Add more dummy products as needed
        )
        _products.value = dummyProducts
    }

    fun addToCart(product: Product) {
        // TODO: Implement add to cart logic
        println("Added ${product.name} to cart")
    }
}

class ProductViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}