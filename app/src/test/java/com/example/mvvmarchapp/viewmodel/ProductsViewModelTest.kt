package com.example.mvvmarchapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.example.mvvmarchapp.model.Data
import com.example.mvvmarchapp.model.Item
import com.example.mvvmarchapp.model.Product
import com.example.mvvmarchapp.others.Resource
import com.example.mvvmarchapp.others.Status
import com.example.mvvmarchapp.repositories.ItemsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class ProductsViewModelTest {

    @Mock
    lateinit var repository: ItemsRepository

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {

        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `getDataFromApi returns empty list`() = runTest {
        Mockito.`when`(repository.getItemsFromRemote()).thenReturn(
            Resource.success(
                Product(
                    Data(
                        emptyList()
                    ), error = Unit, status = "success"
                )
            )
        )

        val sut = ProductsViewModel(repository)
        val result = sut.getDataFromApi()


        assertThat(result.status).isEqualTo(Status.SUCCESS)
        assertThat(result.data?.data?.items).isEmpty()
    }

    @Test
    fun `getDataFromApi returns error`() = runTest {
        Mockito.`when`(repository.getItemsFromRemote()).thenReturn(
            Resource.error("something went wrong", null)
        )

        val sut = ProductsViewModel(repository)
        val result = sut.getDataFromApi()

        assertThat(result.status).isEqualTo(Status.ERROR)
        assertThat(result.message).isEqualTo("something went wrong")
        assertThat(result.data?.data?.items).isNull()
    }

    @Test
    fun `getDataFromApi returns success`() = runTest {
        Mockito.`when`(repository.getItemsFromRemote()).thenReturn(
            Resource.success(
                Product(
                    Data(
                        ItemListsForTest.twoItems
                    ), error = Unit, status = "success"
                )
            )
        )


        val sut = ProductsViewModel(repository)
        val result = sut.getDataFromApi()

        assertThat(result.status).isEqualTo(Status.SUCCESS)
        assertThat(result.data?.data?.items).hasSize(2)
        assertThat(result.data?.data?.items?.get(0)?.name).isEqualTo("Item 1")
    }

    @Test
    fun `getDataFromDatabase returns empty list`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.emptyList)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        val result = sut.getDataFromDatabase()

        assertThat(result.value).isEmpty()
    }

    @Test
    fun `getDataFromDatabase returns null`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        val result = sut.getDataFromDatabase()

        assertThat(result.value).isNull()
    }

    @Test
    fun `getDataFromDatabase returns list`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.threeItems)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        val result = sut.getDataFromDatabase()

        assertThat(result.value).hasSize(3)
        assertThat(result.value?.get(0)?.price).isEqualTo("₹ 200")
    }

    @Test
    fun `updateFromDatabase updates empty livedata`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.emptyList)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        sut.updateFromDatabase()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.totalItems

        assertThat(result.value).isEmpty()
    }


    @Test
    fun `updateFromDatabase updates null livedata`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        sut.updateFromDatabase()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.totalItems.getOrAwaitValueTest()

        assertThat(result).isNull()
    }

    @Test
    fun `updateFromDatabase updates with list livedata`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.threeItems)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        sut.updateFromDatabase()
        testDispatcher.scheduler.advanceUntilIdle()
        val totalItems = sut.totalItems
        val itemsToDisplay = sut.itemsToDisplay

        assertThat(totalItems.value).hasSize(3)
        assertThat(itemsToDisplay.value).hasSize(3)
        assertThat(itemsToDisplay.value?.get(0)?.name).isEqualTo("Item 1")
    }

    @Test
    fun `updateFromApi returns success, updates empty livedata`() = runTest {
        Mockito.`when`(repository.getItemsFromRemote()).thenReturn(
            Resource.success(
                Product(
                    Data(
                        ItemListsForTest.emptyList
                    ), error = Unit, status = "success"
                )
            )
        )

        val sut = ProductsViewModel(repository)
        sut.updateFromApi()

        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.status

        assertThat(result.value?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `updateFromApi returns error`() = runTest {
        Mockito.`when`(repository.getItemsFromRemote()).thenReturn(
            Resource.error("something went wrong", null)
        )

        val sut = ProductsViewModel(repository)
        sut.updateFromApi()

        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.status

        assertThat(result.value?.status).isEqualTo(Status.ERROR)
        assertThat(result.value?.message).isEqualTo("something went wrong")
    }


    @Test
    fun `pass empty list in saveItems, returns false`() {

        val sut =
            ProductsViewModel(repository)
        val res = sut.saveItems(ItemListsForTest.emptyList)
        val totalItems = sut.totalItems

        assertThat(totalItems.value).isNull()
        assertThat(res).isFalse()
    }

    @Test
    fun `pass non empty list in saveItems, returns true`() {
        val sut =
            ProductsViewModel(repository)
        val res = sut.saveItems(ItemListsForTest.oneItemA)
        val totalItems = sut.totalItems

        assertThat(totalItems.value).isNotEmpty()
        assertThat(res).isTrue()
    }

    @Test
    fun `pass same list in saveItems as totalItems, returns false`() = runTest {

        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.oneItemA)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        sut.updateFromDatabase()
        testDispatcher.scheduler.advanceUntilIdle()


        val res = sut.saveItems(ItemListsForTest.oneItemA)
        val totalItems = sut.totalItems
        assertThat(totalItems.value).isNotEmpty()
        assertThat(totalItems.value).hasSize(1)
        assertThat(res).isFalse()
    }

    @Test
    fun `pass diff list in saveItems as totalItems, returns true`() = runTest {

    val _dbLiveData = MutableLiveData<List<Item>?>()
    _dbLiveData.postValue(ItemListsForTest.oneItemA)
    val dbLivedata: LiveData<List<Item>?> = _dbLiveData

    Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

    val sut = ProductsViewModel(repository)
    sut.updateFromDatabase()
    testDispatcher.scheduler.advanceUntilIdle()


    val res = sut.saveItems(ItemListsForTest.threeItems)
    val totalItems = sut.totalItems
    val itemsToDisplay = sut.itemsToDisplay
    assertThat(totalItems.value).isNotEmpty()
    assertThat(totalItems.value).hasSize(3)
    assertThat(itemsToDisplay.value).hasSize(3)
    assertThat(res).isTrue()
    }

    @Test
    fun `compare two equal lists, returns true`() {
        val list1 = ItemListsForTest.oneItemA
        val list2 = ItemListsForTest.oneItemA

        val sut = ProductsViewModel(repository)

        val result = sut.compareListIgnoreOrder(list1, list2)

        assertThat(result).isTrue()
    }

    @Test
    fun `compare two unequal lists, returns false`() {
        val list1 = ItemListsForTest.oneItemA
        val list2 = ItemListsForTest.oneItemB

        val sut = ProductsViewModel(repository)

        val result = sut.compareListIgnoreOrder(list1, list2)

        assertThat(result).isFalse()

    }

    @Test
    fun `pass null string in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.fiveItems)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        sut.updateFromDatabase()
        testDispatcher.scheduler.advanceUntilIdle()

        sut.onSearchTextChanged(null)

        val result = sut.itemsToDisplay

        assertThat(result.value).hasSize(5)
    }

    @Test
    fun `pass empty string string in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.fiveItems)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        sut.updateFromDatabase()
        testDispatcher.scheduler.advanceUntilIdle()

        sut.onSearchTextChanged("")

        val result = sut.itemsToDisplay

        assertThat(result.value).hasSize(5)
    }

    @Test
    fun `pass '4' string string in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.fiveItems)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        sut.updateFromDatabase()
        testDispatcher.scheduler.advanceUntilIdle()

        sut.onSearchTextChanged("4")

        val result = sut.itemsToDisplay

        assertThat(result.value).isEmpty()
    }

    @Test
    fun `pass '7' string string in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.emptyList)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        sut.updateFromDatabase()
        testDispatcher.scheduler.advanceUntilIdle()

        sut.onSearchTextChanged("7")

        val result = sut.itemsToDisplay

        assertThat(result.value).isEmpty()
    }

    @Test
    fun `pass '3' in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.fiveItems)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        sut.updateFromDatabase()
        testDispatcher.scheduler.advanceUntilIdle()

        sut.onSearchTextChanged("3")

        val result = sut.itemsToDisplay

        assertThat(result.value).hasSize(3)
    }

    @Test
    fun `pass '90' in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val _dbLiveData = MutableLiveData<List<Item>?>()
        _dbLiveData.postValue(ItemListsForTest.fiveItems)
        val dbLivedata: LiveData<List<Item>?> = _dbLiveData

        Mockito.`when`(repository.getItemsFromLocal()).thenReturn(dbLivedata)

        val sut = ProductsViewModel(repository)
        sut.updateFromDatabase()
        testDispatcher.scheduler.advanceUntilIdle()

        sut.onSearchTextChanged("90")

        val result = sut.itemsToDisplay

        assertThat(result.value).hasSize(1)
    }

    @Test
    fun `onSwipeLeft check livedata`(){
        val sut = ProductsViewModel(repository)

        sut.onSwipeLeft()

        val result = sut.swipeLeftTrigger

        assertThat(result.value).isEqualTo(Unit)
    }

    @Test
    fun `onSwipeRight check livedata`(){
        val sut = ProductsViewModel(repository)

        sut.onSwipeRight()

        val result = sut.swipeRightTrigger

        assertThat(result.value).isEqualTo(Unit)
    }

}