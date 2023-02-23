package com.example.mvvmarchapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarchapp.data.api.ApiHelper
import com.example.mvvmarchapp.model.Data
import com.example.mvvmarchapp.model.Item
import com.example.mvvmarchapp.data.local.DataBaseHelper
import com.example.mvvmarchapp.model.Product
import com.example.mvvmarchapp.others.Resource
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

    override suspend fun getItemsFromRemote(): Resource<Product> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiHelper.getProducts()
                if (response.isSuccessful) {
                    response.body()?.let {
                        return@let Resource.success(it)
                    } ?: Resource.error("An unknown error occured", null)
                } else {
                    Resource.error("An unknown error occured", null)
                }
            } catch (e: Exception) {
                Log.e("error", e.toString())
                Resource.error("Couldn't reach the server. Check your internet connection", null)
            }
        }
    }
}