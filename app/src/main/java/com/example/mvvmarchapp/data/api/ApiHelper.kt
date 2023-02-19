package com.example.mvvmarchapp.data.api

import com.example.mvvmarchapp.model.Product
import retrofit2.Response

interface ApiHelper {
    suspend fun getProducts(): Response<Product>
}