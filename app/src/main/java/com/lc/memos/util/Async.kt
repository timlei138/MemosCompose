package com.lc.memos.util

sealed class Async<out T> {

    object Loading: Async<Nothing>()

    data class Success<out T>(val data: T) : Async<T>()

    data class Error(val errorCode: Int,val errorMsg: String): Async<Nothing>()
}