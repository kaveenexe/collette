package com.example.colllette.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.colllette.model.Product
import com.example.colllette.model.Cart
import com.example.colllette.model.CartItem
import com.example.colllette.network.ApiClient
import com.example.colllette.network.ProductApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
    application: Application,
    private val userViewModel: UserViewModel // Inject UserViewModel as a parameter
) : AndroidViewModel(application) {
    private val apiClient = ApiClient(application)

    // Get the user's address from the UserViewModel
    fun getUserId(): String {
        return userViewModel.user.value?.id ?: throw IllegalStateException("User ID not available")
    }

    private val productApi: ProductApi = apiClient.productApi


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

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

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

    fun fetchProductDetails(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val product = apiClient.productApi.getProductDetails(productId)
                _selectedProduct.value = product
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to fetch product details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchCartByCartId(cartId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = getUserId() // This will throw an exception if the ID is not available
                val fetchedCart = apiClient.productApi.getCartByUserIdAndCartId(userId, cartId) // Pass the cartId to the API
                _cart.value = fetchedCart
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to fetch cart: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchCart() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = getUserId() // This will throw an exception if the ID is not available
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
                val userId = getUserId() // This will throw an exception if the ID is not available
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
                val userId = getUserId() // This will throw an exception if the ID is not available
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
                    val userId = getUserId() // This will throw an exception if the ID is not available
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
                val userId = getUserId() // This will throw an exception if the ID is not available
                apiClient.productApi.deleteCart(userId)
                _cart.value = null
            } catch (e: Exception) {
                _error.value = "Failed to clear cart: ${e.message}"
            }
        }
    }

}

class ProductViewModelFactory(
    private val application: Application,
    private val userViewModel: UserViewModel
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(application, userViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}