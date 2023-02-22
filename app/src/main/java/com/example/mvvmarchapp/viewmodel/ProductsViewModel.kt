package com.example.mvvmarchapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchapp.model.Item
import com.example.mvvmarchapp.repositories.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val itemsRepository: ItemsRepository) :
    ViewModel() {

    private val _totalItems = MutableLiveData<ArrayList<Item>?>()
    private val _itemsToDisplay = MutableLiveData<ArrayList<Item>?>()
    val items = _itemsToDisplay

    val itemsToDisplay: LiveData<ArrayList<Item>?> = _itemsToDisplay
    val totalItems: LiveData<ArrayList<Item>?> = _totalItems

    private val _swipeLeftTrigger = MutableLiveData<Unit>()
    private val _swipeRightTrigger = MutableLiveData<Unit>()

    val swipeLeftTrigger = _swipeLeftTrigger
    val swipeRightTrigger = _swipeRightTrigger

    init {
        triggerWhenInit()
    }

    //trigger this function at init, can be used for refresh
    fun triggerWhenInit() {
        viewModelScope.launch {
            updateFromDatabase()

            updateFromApi()
        }
    }

    fun onSwipeLeft() {
        _swipeLeftTrigger.postValue(Unit)
    }

    fun onSwipeRight() {
        _swipeRightTrigger.postValue(Unit)
    }

    suspend fun getDataFromDatabase(): LiveData<List<Item>?> {
        return itemsRepository.getItemsFromLocal()
    }

    suspend fun getDataFromApi(): LiveData<List<Item>?> {
        return itemsRepository.getItemsFromRemote()
    }

    suspend fun insertIntoDatabase(itemsList: List<Item>) {
        itemsRepository.insertIntoLocal(itemsList)
    }

    fun saveItems(itemsList: ArrayList<Item>?): Boolean {

        if (!itemsList.isNullOrEmpty()) {
            if (_totalItems.value != null) {
                if (!compareListIgnoreOrder(itemsList, _totalItems.value!!)) {
                    viewModelScope.launch { insertIntoDatabase(itemsList) }
                    _totalItems.value = itemsList
                    _itemsToDisplay.value = _totalItems.value
                    return true
                }
                return false
            } else {
                viewModelScope.launch { insertIntoDatabase(itemsList) }
                _totalItems.postValue(itemsList)
                _itemsToDisplay.value = _totalItems.value
                return true
            }
        }
        return false
    }

    suspend fun updateFromDatabase() {
        _totalItems.value = getDataFromDatabase().value as? ArrayList<Item>
        _itemsToDisplay.value = _totalItems.value
    }

    suspend fun updateFromApi() {
        val apiResult = getDataFromApi().value as? ArrayList<Item>
        saveItems(apiResult)

    }

    fun compareListIgnoreOrder(first: List<Item>, second: List<Item>): Boolean =
        first.size == second.size && first.toSet() == second.toSet()


    fun onSearchTextChanged(newText: String?) {
        if (newText.isNullOrEmpty()) {
            _itemsToDisplay.value = _totalItems.value
            return
        }
        _itemsToDisplay.value =
            _totalItems.value?.filter { it.name.contains(newText) || it.price.contains(newText) } as ArrayList<Item>
    }


}