package com.example.colllette.network

import com.example.colllette.model.Product
import retrofit2.http.GET

interface ProductApi {
    @GET("/api/customer/products") // Adjust this endpoint to match your backend
    suspend fun getProducts(): List<Product>
}