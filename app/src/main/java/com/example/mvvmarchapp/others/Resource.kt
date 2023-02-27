package com.example.mvvmarchapp.others

import com.example.mvvmarchapp.others.Status.Companion.ERROR
import com.example.mvvmarchapp.others.Status.Companion.LOADING
import com.example.mvvmarchapp.others.Status.Companion.SUCCESS


/**
 * Network Resource data class to wrap api response data based on status.
 */
data class Resource<out T>(@Status.Companion.STATUS val status: String, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}