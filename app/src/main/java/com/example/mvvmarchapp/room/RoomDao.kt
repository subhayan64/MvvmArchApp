package com.example.mvvmarchapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mvvmarchapp.model.Item

@Dao
interface RoomDao {

    @Insert
    suspend fun insertItems(items: List<Item>)

    @Query("SELECT * FROM items")
    suspend fun getItems(): List<Item>
}