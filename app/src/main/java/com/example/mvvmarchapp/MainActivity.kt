package com.example.mvvmarchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmarchapp.api.ApiInterface
import com.example.mvvmarchapp.api.ApiUtilities
import com.example.mvvmarchapp.repository.ProductsRepository
import com.example.mvvmarchapp.viewmodel.ProductsViewModel
import com.example.mvvmarchapp.viewmodel.ProductsViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var productsViewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)

        val productsRepository = ProductsRepository(apiInterface)

        productsViewModel = ViewModelProvider(
            this, ProductsViewModelFactory(
                productsRepository
            )
        ).get(ProductsViewModel::class.java)

        productsViewModel.products.observe(this) {
            Log.d("Subhayan", "onCreate: ${it.toString()}")
        }
    }
}