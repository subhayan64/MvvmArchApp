package com.example.mvvmarchapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarchapp.api.ApiHelper
import com.example.mvvmarchapp.model.Data
import com.example.mvvmarchapp.model.Item
import com.example.mvvmarchapp.room.DataBaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

class ProductsRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataBaseHelper: DataBaseHelper
) {
    private val productsLiveData = MutableLiveData<List<Item>?>()

    suspend fun getProducts(): LiveData<List<Item>?> {
        return withContext(Dispatchers.IO) {
            ///TODO: check for network connection here
            val dbResultList = dataBaseHelper.getItems()
            //check if db has data
            if (dbResultList.isNotEmpty()) {
                //send data to ViewModel
                productsLiveData.postValue(Data(dbResultList).items)
                //do api call
                try {
                    val result = apiHelper.getProducts()
                    if (result.body() != null) {
                        val apiResultList = result.body()?.data?.items as List<Item>
                        //check whether data in db same as api response
                        if (!dbResultList.compareListIgnoreOrder(apiResultList)) {
                            //send data to ViewModel
                            productsLiveData.postValue(apiResultList)
                            //store data in db
                            dataBaseHelper.insertItems(apiResultList)
                        }
                        //else - do nothing
                    } else {
                        Log.e("error", "api result is null")
                    }
                } catch (e: Exception) {
                    Log.e("error", e.toString())
                }
            } else {
                //do api call -> send to ViewModel -> store in db
                try {
                    val result = apiHelper.getProducts()
                    if (result.body() != null) {
                        val apiResultList = result.body()?.data?.items as List<Item>
                        productsLiveData.postValue(apiResultList)
                        dataBaseHelper.insertItems(apiResultList)
                    } else {
                        Log.e("error", "api result is null")
                    }
                } catch (e: Exception) {
                    Log.e("error", e.toString())
                }
            }
            productsLiveData
        }
    }

    private fun List<Item>.compareListIgnoreOrder(other: List<Item>) =
        this.size == other.size && this.toSet() == other.toSet()
}