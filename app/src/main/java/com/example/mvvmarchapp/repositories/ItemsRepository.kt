package com.example.mvvmarchapp.repositories

import androidx.lifecycle.LiveData
import com.example.mvvmarchapp.model.Item
import com.example.mvvmarchapp.model.Product
import com.example.mvvmarchapp.others.Resource

interface ItemsRepository {

    suspend fun getItemsFromLocal() : LiveData<List<Item>?>

    suspend fun insertIntoLocal(itemsList: List<Item>)

    suspend fun getItemsFromRemote() : Resource<Product>
}