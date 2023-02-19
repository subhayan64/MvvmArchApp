package com.example.mvvmarchapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarchapp.data.api.ApiHelper
import com.example.mvvmarchapp.model.Data
import com.example.mvvmarchapp.model.Item
import com.example.mvvmarchapp.data.local.DataBaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

class DefaultItemsRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataBaseHelper: DataBaseHelper
) : ItemsRepository {

    override suspend fun getItemsFromLocal(): LiveData<List<Item>?> {
        val itemsLiveData = MutableLiveData<List<Item>?>()
        return withContext(Dispatchers.IO) {
            val dbResultList = dataBaseHelper.getItems()
            itemsLiveData.postValue(Data(dbResultList).items)
            itemsLiveData
        }
    }

    override suspend fun insertIntoLocal(itemsList: List<Item>) {
        dataBaseHelper.insertItems(itemsList)
    }

    override suspend fun getItemsFromRemote(): LiveData<List<Item>?> {
        val itemsLiveData = MutableLiveData<List<Item>?>()
        return withContext(Dispatchers.IO) {
            try {
                val result = apiHelper.getProducts()
                if (result.body() != null) {
                    val apiResultList = result.body()?.data?.items as List<Item>
                    itemsLiveData.postValue(apiResultList)
                } else {
                    Log.e("error", "api result is null")
                }
            } catch (e: Exception) {
                Log.e("error", e.toString())
            }
            itemsLiveData
        }
    }

//
//    suspend fun getProductsFromRemote(): LiveData<List<Item>?> {
//        return withContext(Dispatchers.IO) {
//
//            val dbResultList = dataBaseHelper.getItems()
//            //do api call
//            try {
//                val result = apiHelper.getProducts()
//                if (result.body() != null) {
//                    val apiResultList = result.body()?.data?.items as List<Item>
//                    //check whether data in db is same as api response
//                    if (!dbResultList.compareListIgnoreOrder(apiResultList)) {
//                        //update live data
//                        productsLiveData.postValue(apiResultList)
//                        //store data in db
//                        dataBaseHelper.insertItems(apiResultList)
//                    }
//                    //else - do nothing
//                } else {
//                    Log.e("error", "api result is null")
//                }
//            } catch (e: Exception) {
//                Log.e("error", e.toString())
//            }
//            productsLiveData
//        }
//    }
//
}