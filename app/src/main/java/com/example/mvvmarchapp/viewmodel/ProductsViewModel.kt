package com.example.mvvmarchapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchapp.model.Item
import com.example.mvvmarchapp.model.Product
import com.example.mvvmarchapp.others.Resource
import com.example.mvvmarchapp.others.Status
import com.example.mvvmarchapp.repositories.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ProductsViewModel depends on ItemsRepository, which is injected by dagger-hilt
 * ViewModel responsible for all the BL of the project, Such as:
 *
 * -
 * - [getDataFromApi] calls repository function to get data from remote api.
 * - [insertIntoDatabase] uses repository function to insert list of data into local database.
 * - [updateFromDatabase] gets data from local and updates livedata variables for the UI.
 * - [updateFromApi] gets data from remote and updates _status for UI to get API loading
 *   state, and onSuccess send the data to [saveItems]
 * - [saveItems] validates the data from api, compares it with the persistent data and
 *   insert the same into database and in-turn update the livedata variables.
 * - [compareListIgnoreOrder] takes two lists as parameters and compares their equality
 * - [onSearchTextChanged] gets triggered from the main activity, filters _totalItems livedata
 *   with the passed search string and updates the _itemsToDisplay livedata.
 * - [onSwipeLeft] is triggered from fragment, updates _swipeLeftTrigger livedata
 * - [onSwipeRight] is triggered from fragment, updates _swipeRightTrigger livedata
 */
@HiltViewModel
class ProductsViewModel @Inject constructor(private val itemsRepository: ItemsRepository) :
    ViewModel() {

    private val _totalItems = MutableLiveData<ArrayList<Item>?>()
    private val _itemsToDisplay = MutableLiveData<ArrayList<Item>?>()
    val items = _itemsToDisplay

    val itemsToDisplay: LiveData<ArrayList<Item>?> = _itemsToDisplay
    val totalItems: LiveData<ArrayList<Item>?> = _totalItems

    private val _status = MutableLiveData<Resource<Product>>()
    val status: LiveData<Resource<Product>> = _status

    private val _swipeLeftTrigger = MutableLiveData<Unit>()
    private val _swipeRightTrigger = MutableLiveData<Unit>()

    val swipeLeftTrigger = _swipeLeftTrigger
    val swipeRightTrigger = _swipeRightTrigger

    init {
        triggerWhenInit()
    }

    //trigger this function at init, can be used for refresh
    fun triggerWhenInit() {
        updateFromDatabase()
        updateFromApi()
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

    suspend fun getDataFromApi(): Resource<Product> {
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

    fun updateFromDatabase() {
        viewModelScope.launch {
            _totalItems.value = getDataFromDatabase().value as? ArrayList<Item>
            _itemsToDisplay.value = _totalItems.value
        }
    }

    fun updateFromApi() {
        viewModelScope.launch {
            val apiResult = getDataFromApi()
            _status.postValue(apiResult)
            if (apiResult.status == Status.SUCCESS) {
                saveItems(apiResult.data?.data?.items as? ArrayList<Item>)
            }
        }
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