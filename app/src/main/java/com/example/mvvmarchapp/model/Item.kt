package com.example.mvvmarchapp.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    val extra: String?,
    @PrimaryKey
    val name: String,
    val price: String,
    val image: String
)