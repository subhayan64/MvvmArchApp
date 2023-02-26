package com.example.mvvmarchapp.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmarchapp.data.api.ApiHelper
import com.example.mvvmarchapp.data.local.DataBaseHelper
import com.example.mvvmarchapp.model.Data
import com.example.mvvmarchapp.model.Product
import com.example.mvvmarchapp.others.Status
import com.example.mvvmarchapp.ItemListsForTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class ItemsRepositoryTest {


    @Mock
    lateinit var apiHelper: ApiHelper

    @Mock
    lateinit var dataBaseHelper: DataBaseHelper

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `getItemsFromRemote returns success with an empty list`() = runTest {

        Mockito.`when`(apiHelper.getProducts()).thenReturn(
            Response.success(Product(Data(emptyList()), Unit, "success"))
        )
        val sut = ItemsRepository(apiHelper, dataBaseHelper)

        val result = sut.getItemsFromRemote()

        assertThat(result.status).isEqualTo(Status.SUCCESS)
        assertThat(result.data?.data?.items).isNotNull()
        assertThat(result.data?.data?.items).isEmpty()
    }

    @Test
    fun `getItemsFromRemote returns success with a list`() = runTest {

        Mockito.`when`(apiHelper.getProducts()).thenReturn(
            Response.success(Product(Data(ItemListsForTest.twoItems), Unit, "success"))
        )
        val sut = ItemsRepository(apiHelper, dataBaseHelper)

        val result = sut.getItemsFromRemote()

        assertThat(result.status).isEqualTo(Status.SUCCESS)
        assertThat(result.data?.data?.items).isNotEmpty()
        assertThat(result.data?.data?.items).hasSize(2)
    }

    @Test
    fun `getItemsFromRemote returns error`() = runTest {

        val errorResponse = "{\"key\":[\"somestuff\"]}"
            .toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.`when`(apiHelper.getProducts()).thenReturn(
            Response.error(
                403,
                errorResponse
            )
        )
        val sut = ItemsRepository(apiHelper, dataBaseHelper)

        val result = sut.getItemsFromRemote()

        assertThat(result.status).isEqualTo(Status.ERROR)
        assertThat(result.message).isEqualTo("An unknown error occured")
    }


    @Test
    fun `getItemsFromLocal returns empty list`() = runTest {

        Mockito.`when`(dataBaseHelper.getItems()).thenReturn(
           emptyList()
        )
        val sut = ItemsRepository(apiHelper, dataBaseHelper)

        val result = sut.getItemsFromLocal()

        assertThat(result.value).isEmpty()
    }

    @Test
    fun `getItemsFromLocal returns list with one item`() = runTest {

        Mockito.`when`(dataBaseHelper.getItems()).thenReturn(
            ItemListsForTest.oneItemA
        )
        val sut = ItemsRepository(apiHelper, dataBaseHelper)

        val result = sut.getItemsFromLocal()

        assertThat(result.value).isNotEmpty()
        assertThat(result.value).hasSize(1)
    }

    @Test
    fun `getItemsFromLocal returns list with multiple items`() = runTest {

        Mockito.`when`(dataBaseHelper.getItems()).thenReturn(
            ItemListsForTest.threeItems
        )
        val sut = ItemsRepository(apiHelper, dataBaseHelper)

        val result = sut.getItemsFromLocal()

        assertThat(result.value).isNotEmpty()
        assertThat(result.value).hasSize(3)
        assertThat(result.value?.get(0)?.name).isEqualTo("Item 1")
        assertThat(result.value?.get(1)?.extra).isNull()
        assertThat(result.value?.get(1)?.extra).isNull()
        assertThat(result.value?.get(2)?.image).isEmpty()
    }
}

