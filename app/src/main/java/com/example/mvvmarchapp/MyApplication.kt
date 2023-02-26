package com.example.mvvmarchapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Dagger-hilt to inject applicationContext
 */
@HiltAndroidApp
class MyApplication : Application() {
}