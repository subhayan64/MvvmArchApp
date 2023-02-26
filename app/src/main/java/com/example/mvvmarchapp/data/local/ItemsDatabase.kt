package com.example.mvvmarchapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvmarchapp.model.Item

/**
 * Contains the database and serves as the main access point for the underlying
 * connection to the local data.
 */
@Database(entities = [Item::class], version = 1)
abstract class ItemsDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemsDao
}