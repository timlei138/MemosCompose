package com.lc.memos.data.api

import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

//@Deprecated("", level = DeprecationLevel.ERROR)
//sealed class ApiResponse<out T>{
//
//    companion object {
//        fun create(code: Int, msg: String?, error: Exception? = null): Failed {
//            return Failed(code, msg, error)
//        }
//
//        fun <T> create(data: T): Success<T> {
//            return Success(data)
//        }
//    }
//}
//
//
//@Deprecated("", level = DeprecationLevel.ERROR)
//data class Success<T>(val data: T) : ApiResponse<T>(){
//
//}
//
//data class Failed(val code: Int, val msg: String? = "", val error: Exception? = null) :
//    ApiResponse<Nothing>()
//
//
//
//
//
//suspend inline fun <T> ApiResponse<T>.suspendOnSuccess(crossinline onResult: suspend Success<T>.() -> Unit): ApiResponse<T> {
//    if (this is Success){
//        onResult(this)
//    }
//    return this
//}
//
//suspend inline fun <T> ApiResponse<T>.suspendOnNotLogin(crossinline block: suspend Failed.() -> Unit): ApiResponse<T> {
//    if (this is Failed){
//        block(this)
//    }
//    return this
//}
