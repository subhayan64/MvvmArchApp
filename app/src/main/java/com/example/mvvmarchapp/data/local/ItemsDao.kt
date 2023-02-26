package com.example.mvvmarchapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmarchapp.model.Item

/**
 * Data Access Object interface to define all the database interactions
 * - [insertItems] inserts list of [Item].
 *   Replaces the whole entry on conflict in that primaryKey.
 * - [getItems] gets all the Items from the database table, returns List of [Item].
 */
@Dao
interface ItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<Item>)

    @Query("SELECT * FROM items")
    suspend fun getItems(): List<Item>

}