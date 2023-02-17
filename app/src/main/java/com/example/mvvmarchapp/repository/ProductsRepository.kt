package com.example.mvvmarchapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarchapp.api.ApiHelper
import com.example.mvvmarchapp.model.Data
import com.example.mvvmarchapp.model.Product
import com.example.mvvmarchapp.room.DataBaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataBaseHelper: DataBaseHelper
) {
    private val productsLiveData = MutableLiveData<Product?>()

    suspend fun getProducts(): LiveData<Product?> {
        return withContext(Dispatchers.IO) {
            ///TODO: check for network connection here
            if (true) {
                //when network is available do api call and store data in db
                try {
                    val result = apiHelper.getProducts()
                    if (result.body() != null) {
                        //to store response data in the db
                        dataBaseHelper.insertItems(result.body()!!.data.items)

                        //to update the livedata with api response
                        productsLiveData.postValue(result.body())
                    }
                } catch (e: Exception) {
                    Log.e("error", e.toString())
                }
            } else {
                //in case of no network: fetch from db
                val products = dataBaseHelper.getItems()

                productsLiveData.postValue(Product(Data(products), Unit, "success"))
            }
            productsLiveData
        }
    }
}