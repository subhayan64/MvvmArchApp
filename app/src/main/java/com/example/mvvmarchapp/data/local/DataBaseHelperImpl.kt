package com.example.mvvmarchapp.data.local

import com.example.mvvmarchapp.model.Item
import javax.inject.Inject

/**
 * Implementation class for [DataBaseHelper]. The [ItemsDao] dependency is injected with
 * Dagger-hilt
 */
class DataBaseHelperImpl @Inject constructor(private val itemsDao: ItemsDao) : DataBaseHelper {
    override suspend fun insertItems(items: List<Item>) {
        itemsDao.insertItems(items)
    }

    override suspend fun getItems(): List<Item> {
        return itemsDao.getItems()
    }

}