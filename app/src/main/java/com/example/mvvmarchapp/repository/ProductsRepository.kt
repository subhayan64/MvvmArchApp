package com.example.mvvmarchapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarchapp.api.ApiInterface
import com.example.mvvmarchapp.model.Data
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
        ///TODO: check for network connection here
        if (true) {
            //when network is available do api call and store data in db
            val result = apiInterface.getProducts()
            if (result.body() != null) {

                //to store response data in the db
                itemsDatabase.itemDao().insertItems(result.body()!!.data.items)

                //to update the livedata with api response
                productsLiveData.postValue(result.body())
            }
        } else {
            //in case of no network: fetch from db
            val products = itemsDatabase.itemDao().getItems()

            val productsList = Product(Data(products), Unit, "success")

            productsLiveData.postValue(productsList)
        }


    }

}