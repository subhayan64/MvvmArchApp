package com.example.mvvmarchapp.api

import com.example.mvvmarchapp.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("b6a30bb0-140f-4966-8608-1dc35fa1fadc")
    suspend fun getProducts(): Response<Product>
}