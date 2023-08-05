package com.lc.memos.data.api

import com.lc.memos.util.ApiResponse
import com.lc.memos.util.Failed
import com.lc.memos.util.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class RetrofitDSL<T> {

    internal lateinit var api: (suspend () -> ApiResponse<T>)
        private set
    internal var onSuccess: ((T) -> Unit)? = null
        private set
    internal var onFailed: ((code: Int,msg: String?,e: Exception?) -> Unit)? = null
        private set


    fun call(block: suspend  () -> ApiResponse<T>){
        this.api = block
    }

    fun onSuccess(block: (T) -> Unit){
        this.onSuccess = block
    }

    fun onFailed(block: ((code: Int,msg: String?,e: Exception?) -> Unit)){
        this.onFailed = block
    }

}

fun <T> CoroutineScope.safeCall(dsl: RetrofitDSL<T>.() -> Unit){
    launch {
        val retrofitDsl = RetrofitDSL<T>()
        retrofitDsl.dsl()
        try {
            val result = retrofitDsl.api()
            Timber.d("result $result")
            when(result){
                is Success -> retrofitDsl.onSuccess?.invoke(result.data)
                is Failed -> retrofitDsl.onFailed?.invoke(result.code,result.msg,result.error)
            }
        }catch (e: Exception){
            e.printStackTrace()
            //retrofitDsl.onFailed { code, msg, exception ->  }
            retrofitDsl.onFailed?.invoke(-1,"",e)
        }
    }
}

