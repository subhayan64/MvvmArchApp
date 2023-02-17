package com.example.mvvmarchapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchapp.model.Item
import com.example.mvvmarchapp.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productsRepository: ProductsRepository) :
    ViewModel() {
    private val _items = MutableLiveData<ArrayList<Item>?>()
    val items = _items

    init {
        viewModelScope.launch {
            items.value = productsRepository.getProducts().value?.data?.items as ArrayList<Item>
        }
    }


}