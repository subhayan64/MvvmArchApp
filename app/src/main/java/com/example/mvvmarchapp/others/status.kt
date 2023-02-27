package com.example.mvvmarchapp.others

import androidx.annotation.StringDef


class Status{
    companion object{
        @StringDef(SUCCESS, ERROR, LOADING)
        @Retention(AnnotationRetention.SOURCE)
        annotation class STATUS

        const val SUCCESS = "SUCCESS"
        const val ERROR = "ERROR"
        const val LOADING = "LOADING"
    }
}
