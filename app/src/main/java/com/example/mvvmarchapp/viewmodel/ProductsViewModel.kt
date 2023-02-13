package com.example.mvvmarchapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchapp.model.Product
import com.example.mvvmarchapp.repository.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel(private val productsRepository: ProductsRepository) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            productsRepository.getProducts()
        }
    }

    val products: LiveData<Product>
        get() = productsRepository.products
}