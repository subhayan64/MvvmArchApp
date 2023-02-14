package com.example.mvvmarchapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarchapp.api.ApiInterface
import com.example.mvvmarchapp.model.Product
import com.example.mvvmarchapp.room.ItemsDatabase

class ProductsRepository(
    private val apiInterface: ApiInterface,
    private val itemsDatabase: ItemsDatabase
) {
    private val productsLiveData = MutableLiveData<Product>()


    val products: LiveData<Product>
        get() = productsLiveData

    suspend fun getProducts() {
        val result = apiInterface.getProducts()
        if (result.body() != null) {

            itemsDatabase.itemDao().insertItems(result.body()!!.data.items)

            productsLiveData.postValue(result.body())
        }
    }

}