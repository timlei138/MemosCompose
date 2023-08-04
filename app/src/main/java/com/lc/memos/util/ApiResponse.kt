package com.lc.memos.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class ApiResponse<out T>{

    companion object {
        fun create(code: Int, msg: String?, error: Exception? = null): Failed {
            return Failed(code, msg, error)
        }

        fun <T> create(data: T): Success<T> {
            return Success(data)
        }
    }
}


data class Success<T>(val data: T) : ApiResponse<T>()

data class Failed(val code: Int, val msg: String? = "", val error: Exception? = null) :
    ApiResponse<Nothing>()


//fun <T> CoroutineScope.safeCall(
//    dispatcher: CoroutineDispatcher = Dispatchers.Default,
//    block: suspend CoroutineScope.() -> ApiResponse<T>,
//): T {
//    launch(dispatcher) {
//        try {
//            val result = block()
//            if (result.)
//        } catch (e: Exception) {
//
//        }
//    }
//}