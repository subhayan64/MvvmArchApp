package com.example.mvvmarchapp.data.local

import com.example.mvvmarchapp.model.Item

interface DataBaseHelper {

    suspend fun insertItems(items: List<Item>)

    suspend fun getItems(): List<Item>

}