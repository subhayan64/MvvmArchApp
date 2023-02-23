package com.example.mvvmarchapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarchapp.model.Item

//class FakeItemsRepository : ItemsRepository {
//    private val itemsLiveData = MutableLiveData<List<Item>?>()
//    override suspend fun getItemsFromLocal(): LiveData<List<Item>?> {
//        return itemsLiveData
//    }
//
//    override suspend fun insertIntoLocal(itemsList: List<Item>) {
//        itemsLiveData.postValue(itemsList)
//    }
//
//    override suspend fun getItemsFromRemote(): LiveData<List<Item>?> {
//        return itemsLiveData
//    }
//}