package com.example.mvvmarchapp.room

import com.example.mvvmarchapp.model.Item

interface DataBaseHelper {

    suspend fun insertItems(items: List<Item>)

    suspend fun getItems(): List<Item>

    suspend fun deleteItem()
}