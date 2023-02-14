package com.example.mvvmarchapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvmarchapp.model.Item

@Database(entities = [Item::class], version = 1)
abstract class ItemsDatabase : RoomDatabase() {

    abstract fun itemDao () : RoomDao

    companion object {
        private var INSTANCE: ItemsDatabase? = null
        fun getDatabase(context: Context): ItemsDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    ItemsDatabase::class.java,
                    "itemsDB"
                ).build()
            }
            return INSTANCE!!
        }
    }
}