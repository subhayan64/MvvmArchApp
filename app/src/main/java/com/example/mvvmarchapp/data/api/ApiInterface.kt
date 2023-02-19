package com.example.mvvmarchapp.data.api

import com.example.mvvmarchapp.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("995ce2a0-1daf-4993-915f-8c198f3f752c")
    suspend fun getProducts(): Response<Product>
}