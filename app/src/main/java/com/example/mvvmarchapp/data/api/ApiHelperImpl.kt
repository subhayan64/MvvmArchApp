package com.example.mvvmarchapp.data.api

import com.example.mvvmarchapp.model.Product
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiInterface: ApiInterface) : ApiHelper {
    override suspend fun getProducts(): Response<Product> {
        return apiInterface.getProducts()
    }
}