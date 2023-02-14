package com.example.mvvmarchapp

import android.app.Application
import com.example.mvvmarchapp.api.ApiInterface
import com.example.mvvmarchapp.api.ApiUtilities
import com.example.mvvmarchapp.repository.ProductsRepository
import com.example.mvvmarchapp.room.ItemsDatabase

class MyApplication : Application() {

    lateinit var productsRepository: ProductsRepository
    override fun onCreate() {
        super.onCreate()

        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)

        val database = ItemsDatabase.getDatabase(applicationContext)

        productsRepository = ProductsRepository(apiInterface, database)
    }
}