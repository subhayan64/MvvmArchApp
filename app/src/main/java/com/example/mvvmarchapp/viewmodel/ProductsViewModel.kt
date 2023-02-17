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

    private val _totalItems = MutableLiveData<ArrayList<Item>?>()
    private val _searchedItems = MutableLiveData<ArrayList<Item>?>()
    val items = _searchedItems

    init {

        viewModelScope.launch {
            _totalItems.value =
                productsRepository.getProducts().value?.data?.items as? ArrayList<Item>
            _searchedItems.value = _totalItems.value

        }

    }

    fun onSearchTextChanged(newText: String?) {
        if (newText.isNullOrEmpty()) {
            _searchedItems.value = _totalItems.value
            return
        }
        _searchedItems.value =
            _totalItems.value?.filter { it.name.contains(newText) || it.price.contains(newText) } as ArrayList<Item>
    }


}