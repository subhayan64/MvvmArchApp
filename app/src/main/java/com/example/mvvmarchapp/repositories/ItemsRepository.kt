package com.example.mvvmarchapp.repositories

import androidx.lifecycle.LiveData
import com.example.mvvmarchapp.model.Item

interface ItemsRepository {

    suspend fun getItemsFromLocal() : LiveData<List<Item>?>

    suspend fun insertIntoLocal(itemsList: List<Item>)

    suspend fun getItemsFromRemote() : LiveData<List<Item>?>

}