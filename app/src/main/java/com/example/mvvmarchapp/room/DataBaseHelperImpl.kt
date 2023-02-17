package com.example.mvvmarchapp.room

import com.example.mvvmarchapp.model.Item
import javax.inject.Inject

class DataBaseHelperImpl @Inject constructor(private val roomDao: RoomDao) : DataBaseHelper {
    override suspend fun insertItems(items: List<Item>) {
        roomDao.insertItems(items)
    }

    override suspend fun getItems(): List<Item> {
        return roomDao.getItems()
    }

}