package com.example.mvvmarchapp.data.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiInterfaceTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var apiInterface: ApiInterface

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        apiInterface = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getProducts returns success`() = runBlocking{
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/responseSuccess.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = apiInterface.getProducts()
        mockWebServer.takeRequest()


        assertThat(response.code()).isEqualTo(200)
        assertThat(response.body()?.data?.items).isNotEmpty()
        assertThat(response.body()?.data?.items).hasSize(9)
    }

    @Test
    fun `getProducts returns error`() = runBlocking{
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/responseSuccess.json")
        mockResponse.setResponseCode(403)
        mockResponse.setBody("Something went wrong")
        mockWebServer.enqueue(mockResponse)

        val response = apiInterface.getProducts()
        mockWebServer.takeRequest()


        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(403)
    }
}