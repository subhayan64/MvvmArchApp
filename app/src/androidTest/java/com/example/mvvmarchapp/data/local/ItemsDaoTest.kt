package com.example.mvvmarchapp.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.mvvmarchapp.model.Item
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ItemsDaoTest {
    private lateinit var itemsDatabase: ItemsDatabase
    private lateinit var itemsDao: ItemsDao

    @Before
    fun setup() {
        itemsDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ItemsDatabase::class.java
        ).allowMainThreadQueries().build()
        itemsDao = itemsDatabase.itemDao()
    }

    @After
    fun teardown() {
        itemsDatabase.close()
    }

    @Test
    fun getItems_expectedNoItems() = runBlocking {
        val allItems = itemsDao.getItems()

        assertThat(allItems).isEmpty()
    }

    @Test
    fun getItems_expectedNotNull() = runBlocking {
        val allItems = itemsDao.getItems()

        assertThat(allItems).isNotNull()
    }

    @Test
    fun insertItem_expectedSize() = runBlocking {
        val itemList = mutableListOf<Item>()
        itemList.add(
            Item(
                null,
                "Item 1",
                "₹ 100",
                "https://imgstatic.phonepe.com/images/dark/app-icons-ia-1/transfers/80/80/ic_check_balance.png"
            )
        )
        itemList.add(
            Item(
                "Same day shipping",
                "Item 2",
                "₹ 100",
                "https://imgstatic.phonepe.com/images/dark/app-icons-ia-1/transfers/80/80/ic_check_balance.png"
            )
        )

        itemsDao.insertItems(itemList)

        val allItems = itemsDao.getItems()

        assertThat(allItems).hasSize(2)
    }

    @Test
    fun insertItem_expectedItemName() = runBlocking {
        val itemList = mutableListOf<Item>()
        itemList.add(
            Item(
                null,
                "Item 2",
                "₹ 100",
                "https://imgstatic.phonepe.com/images/dark/app-icons-ia-1/transfers/80/80/ic_check_balance.png"
            )
        )

        itemsDao.insertItems(itemList)

        val allItems = itemsDao.getItems()

        assertThat(allItems[0].extra).isNull()
        assertThat(allItems[0].name).isEqualTo("Item 2")
        assertThat(allItems[0].price).isEqualTo("₹ 100")
        assertThat(allItems[0].image).isEqualTo("https://imgstatic.phonepe.com/images/dark/app-icons-ia-1/transfers/80/80/ic_check_balance.png")

    }

}