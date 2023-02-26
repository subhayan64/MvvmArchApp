package com.example.mvvmarchapp.data.local

import com.example.mvvmarchapp.model.Item

/**
 * Helper interface to have a generic implementation of Database interaction methods
 */
interface DataBaseHelper {

    suspend fun insertItems(items: List<Item>)

    suspend fun getItems(): List<Item>

}