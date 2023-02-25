package com.example.mvvmarchapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvmarchapp.others.Constants

@Entity(tableName = Constants.TABLE_NAME)
data class Item(
    @ColumnInfo(name = "extra")
    val extra: String?,
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: String,
    @ColumnInfo(name = "image")
    val image: String
)