package com.example.mvvmarchapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.MainCoroutineRule
import com.example.mvvmarchapp.repositories.FakeItemsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ProductsViewModelTest {

    private lateinit var productsViewModel: ProductsViewModel


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        productsViewModel = ProductsViewModel(FakeItemsRepository())
    }


    @Test
    fun `pass null list in saveItems, returns false`() {
        val res =
            productsViewModel.saveItems(null)
        assertThat(res).isFalse()
    }

    @Test
    fun `pass empty list in saveItems, returns false`() {
        val itemsList = ItemListsForTest.emptyList

        val res =
            productsViewModel.saveItems(itemsList)
        assertThat(res).isFalse()
    }

    @Test
    fun `pass non empty list in saveItems, returns true`() {
        val itemsList = ItemListsForTest.oneItemA

        val res =
            productsViewModel.saveItems(itemsList)
        print(res)
        assertThat(res).isTrue()
    }

    @Test
    fun `pass same list in saveItems as totalItems, returns false`() = runTest {


        val insertionList = ItemListsForTest.oneItemA
        //inserting a list into db
        productsViewModel.insertIntoDatabase(insertionList)
        //fetching from db and updating the livedata
        productsViewModel.updateFromDatabase()

        val itemsList = ItemListsForTest.oneItemA

        //trying to save the same data as available in db
        val res =
            productsViewModel.saveItems(itemsList)
        assertThat(res).isFalse()
    }

    @Test
    fun `insert into database check value from getDataFromDatabase, returns size 1`() = runTest{
        val insertionList = ItemListsForTest.oneItemA
        productsViewModel.insertIntoDatabase(insertionList)
        val res = productsViewModel.getDataFromDatabase()

        assertThat(res.value).hasSize(1)
        assertThat(res.value?.get(0)?.name).isEqualTo("Item 1")
    }

    @Test
    fun `pass diff list in saveItems as totalItems, returns true`() = runTest {


        val insertionList = ItemListsForTest.oneItemA
        //inserting a list into db
        productsViewModel.insertIntoDatabase(insertionList)
        //fetching from db and updating the livedata
        productsViewModel.updateFromDatabase()

        val itemsList = ItemListsForTest.twoItems

        //trying to save the same data as available in db
        val res =
            productsViewModel.saveItems(itemsList)
        assertThat(res).isTrue()
    }


    @Test
    fun `pass diff list in saveItems than totalItems, returns true, check totalItems size`() =
        runTest {
            //inserting a list into db
            productsViewModel.insertIntoDatabase(ItemListsForTest.twoItems)
            //fetching from db and updating the livedata
            productsViewModel.updateFromDatabase()

            //trying to save the same data as available in db
            val res =
                productsViewModel.saveItems(ItemListsForTest.threeItems)
            assertThat(res).isTrue()
            //checking the size of the livedata
            assertThat(productsViewModel.totalItems.value).hasSize(3)
            assertThat(productsViewModel.itemsToDisplay.value).hasSize(3)
        }


    @Test
    fun `compare two equal lists, returns true`() {
        val list1 = ItemListsForTest.oneItemA
        val list2 = ItemListsForTest.oneItemA

        val res = productsViewModel.compareListIgnoreOrder(list1, list2)


        assertThat(res).isTrue()
    }

    @Test
    fun `compare two unequal lists, returns false`() {
        val list1 = ItemListsForTest.oneItemA
        val list2 = ItemListsForTest.oneItemB

        val res = productsViewModel.compareListIgnoreOrder(list1, list2)

        assertThat(res).isFalse()

    }

    @Test
    fun `pass empty string in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val insertionList = ItemListsForTest.fiveItems
        //inserting a list into db
        productsViewModel.insertIntoDatabase(insertionList)
        //fetching from db and updating the livedata
        productsViewModel.updateFromDatabase()

        //trying to save the same data as available in db

        productsViewModel.onSearchTextChanged("")
        val result = productsViewModel.itemsToDisplay.value

        assertThat(result).hasSize(5)
    }

    @Test
    fun `pass null string in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val insertionList = ItemListsForTest.fiveItems
        //inserting a list into db
        productsViewModel.insertIntoDatabase(insertionList)
        //fetching from db and updating the livedata
        productsViewModel.updateFromDatabase()

        //trying to save the same data as available in db

        productsViewModel.onSearchTextChanged(null)
        val result = productsViewModel.itemsToDisplay.value

        assertThat(result).hasSize(5)
    }


    @Test
    fun `pass '3' in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val insertionList = ItemListsForTest.fiveItems
        //inserting a list into db
        productsViewModel.insertIntoDatabase(insertionList)
        //fetching from db and updating the livedata
        productsViewModel.updateFromDatabase()

        //trying to save the same data as available in db

        productsViewModel.onSearchTextChanged("3")
        val result = productsViewModel.itemsToDisplay.value

        assertThat(result).hasSize(3)
    }

    @Test
    fun `pass '90' in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val insertionList = ItemListsForTest.fiveItems
        //inserting a list into db
        productsViewModel.insertIntoDatabase(insertionList)
        //fetching from db and updating the livedata
        productsViewModel.updateFromDatabase()

        //trying to save the same data as available in db

        productsViewModel.onSearchTextChanged("90")
        val result = productsViewModel.itemsToDisplay.value

        assertThat(result).hasSize(1)
    }

    @Test
    fun `pass 'Item' in onSearchTextChanged, check _itemsToDisplay size`() = runTest {
        val insertionList = ItemListsForTest.fiveItems
        //inserting a list into db
        productsViewModel.insertIntoDatabase(insertionList)
        //fetching from db and updating the livedata
        productsViewModel.updateFromDatabase()

        //trying to save the same data as available in db

        productsViewModel.onSearchTextChanged("Item")
        val result = productsViewModel.itemsToDisplay.value

        assertThat(result).hasSize(5)
    }

}