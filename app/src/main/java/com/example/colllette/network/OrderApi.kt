package com.example.colllette.network

import com.example.colllette.model.Order
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {
    @POST("api/Orders/customer")
    suspend fun createOrder(@Body order: Order): Order

    @GET("api/Orders/{id}")
    suspend fun getOrderById(@Path("id") orderId: String): Order

    @GET("api/Orders/customer/{customerId}/order/{orderId}")
    suspend fun getOrderByCustomerIdAndOrderId(
        @Path("customerId") customerId: String,
        @Path("orderId") orderId: String
    ): Order?

    @GET("api/Orders/customer/{customerId}")
    suspend fun getOrdersByCustomerId(@Path("customerId") customerId: String): List<Order>

    @POST("api/Orders/request-cancel")
    suspend fun requestOrderCancellation(@Body cancellation: OrderCancellation): Response<Void>
}
