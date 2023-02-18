package com.example.mvvmarchapp.di

import android.content.Context
import androidx.room.Room
import com.example.mvvmarchapp.data.api.ApiHelper
import com.example.mvvmarchapp.data.api.ApiHelperImpl
import com.example.mvvmarchapp.data.api.ApiInterface
import com.example.mvvmarchapp.others.Constants
import com.example.mvvmarchapp.data.local.DataBaseHelper
import com.example.mvvmarchapp.data.local.DataBaseHelperImpl
import com.example.mvvmarchapp.data.local.ItemsDatabase
import com.example.mvvmarchapp.data.local.ItemsDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {


    companion object {

        @Provides
        fun provideBaseUrl() = Constants.BASE_URL

        @Provides
        @Singleton
        fun provideMyApi(BASE_URL: String): ApiInterface {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }

        @Provides
        fun provideRoomDao(itemsDatabase: ItemsDatabase): ItemsDao = itemsDatabase.itemDao()

        @Provides
        @Singleton
        fun provideItemsDatabase(@ApplicationContext context: Context): ItemsDatabase {
            return Room.databaseBuilder(
                context,
                ItemsDatabase::class.java,
                "itemsDB"
            ).build()
        }

    }

    @Binds
    @Singleton
    abstract fun provideApiHelper(apiHelperImpl: ApiHelperImpl): ApiHelper

    @Binds
    @Singleton
    abstract fun provideDataBaseHelper(dataBaseHelperImpl: DataBaseHelperImpl) : DataBaseHelper


}