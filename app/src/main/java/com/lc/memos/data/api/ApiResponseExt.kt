package com.lc.memos.data.api

import com.lc.memos.ext.MemosException
import com.lc.mini.call.ApiResponse
import com.lc.mini.call.exceptions.ErrorMessage
import com.lc.mini.call.message
import com.lc.mini.call.retrofit.errorBody
import com.lc.mini.call.serialization.deserializeErrorBody
import timber.log.Timber

fun <T> ApiResponse<T>.getErrorMessage(): String {
    if (this is ApiResponse.Failure.Error) {
        try {
            val errorMessage: ErrorMessage? = this.deserializeErrorBody()
            if (errorMessage != null) {
                return errorMessage.message
            }
        } catch (e: Throwable) {
            Timber.d(e)
        }
        return errorBody?.string()  ?: message()
    }

    if (this is ApiResponse.Failure.Exception) {
        return message ?: throwable.message ?: ""
    }
    return ""
}

suspend inline fun <T> ApiResponse<T>.suspendOnErrorMessage(crossinline block: suspend (message: String) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Failure.Error) {
        block(getErrorMessage())
    } else if (this is ApiResponse.Failure.Exception) {
        block(getErrorMessage())
    }

    return this
}

suspend inline fun <T> ApiResponse<T>.suspendOnNotLogin(crossinline block: suspend ApiResponse.Failure<T>.() -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Failure.Exception) {
        if (this.throwable == MemosException.noLogin) {
            block(this as ApiResponse.Failure)
        }
    }
    if (this is ApiResponse.Failure.Error) {

    }
    return this
}