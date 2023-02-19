package com.example.mvvmarchapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvmarchapp.others.Constants

@Entity(tableName = Constants.TABLE_NAME)
data class Item(
    val extra: String?,
    @PrimaryKey
    val name: String,
    val price: String,
    val image: String
)