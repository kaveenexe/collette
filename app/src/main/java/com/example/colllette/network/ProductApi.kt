package com.example.colllette.network

import com.example.colllette.model.Product
import com.example.colllette.model.Cart
import com.example.colllette.model.CartItem
import retrofit2.http.*

interface ProductApi {
    @GET("api/customer/products")
    suspend fun getProducts(): List<Product>

    @GET("api/customer/products/{id}")
    suspend fun getProductDetails(@Path("id") productId: String): Product

    @GET("api/Cart/{userId}")
    suspend fun getCart(@Path("userId") userId: String): Cart

    @DELETE("api/Cart/{userId}")
    suspend fun deleteCart(@Path("userId") userId: String)

    @POST("api/Cart/{userId}/items")
    suspend fun addToCart(@Path("userId") userId: String, @Body item: CartItem)

    @DELETE("api/Cart/{userId}/items/{productId}")
    suspend fun removeFromCart(@Path("userId") userId: String, @Path("productId") productId: String)

    @PUT("api/Cart/{userId}/items/{productId}")
    suspend fun updateCartItem(@Path("userId") userId: String, @Path("productId") productId: String, @Body item: CartItem)
}