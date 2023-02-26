package com.example.mvvmarchapp.data.api

import com.example.mvvmarchapp.model.Product
import retrofit2.Response

/**
 * Helper interface to have a generic implementation of API request methods
 */
interface ApiHelper {
    suspend fun getProducts(): Response<Product>
}