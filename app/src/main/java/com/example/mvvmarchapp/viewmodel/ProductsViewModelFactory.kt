package com.example.mvvmarchapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmarchapp.repository.ProductsRepository

class ProductsViewModelFactory(private val productsRepository: ProductsRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductsViewModel(productsRepository) as T
    }
}