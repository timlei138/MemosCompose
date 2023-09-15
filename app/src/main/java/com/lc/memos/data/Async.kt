package com.lc.memos.data

sealed class Async<out T> {

    object Loading: Async<Nothing>()

    data class Success<out T>(val data: T) : Async<T>()

    data class Error(val errorCode: Int,val errorMsg: String?): Async<Nothing>()
}