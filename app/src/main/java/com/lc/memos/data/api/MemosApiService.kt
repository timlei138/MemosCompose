package com.lc.memos.data.api

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.lc.memos.data.Status
import com.lc.memos.di.ApplicationScope
import com.lc.memos.ext.DataStoreKeys
import com.lc.memos.ext.MemosException
import com.lc.memos.ext.dataStore
import com.lc.mini.call.ApiResponse
import com.lc.mini.call.retrofit.adapters.ApiResponseCallAdapterFactory
import com.lc.mini.call.suspendOnSuccess
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemosApiService @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val scope: CoroutineScope,
    private val okHttpClient: OkHttpClient
) {

    private var memosApi: MemosApi? = null
    var status: Status? = null
        private set
    var host: String? = null
        private set
    var client: OkHttpClient = okHttpClient
        private set

    var token: String? = null
        private set

    private val mutex = Mutex()



    private fun loadStatus() = scope.launch {
        memosApi?.status()?.suspendOnSuccess {
            status = data
        }
    }

    init {
        runBlocking {
            Timber.d("runBlocking")
            context.dataStore.data.first().let {
                val host = it[DataStoreKeys.Host.key]
                val openId = it[DataStoreKeys.OpenId.key]
                token = it[DataStoreKeys.AccountToken.key]
                if (host?.isNotEmpty() == true){
                    val(_client,api) = createClient(host,openId)
                    memosApi = api
                    this@MemosApiService.host = host
                    client = _client
                }
            }
        }
        loadStatus()
    }


    fun createClient(host: String,openId: String? = ""): Pair<OkHttpClient,MemosApi>{
        val client = okHttpClient

        return client to Retrofit.Builder().baseUrl(host).client(okHttpClient)
            //.addConverterFactory(ApiGsonConverterFactory()).build()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create()).build()
            .create(MemosApi::class.java)
    }


    suspend fun update(host: String,openId: String?){
        context.dataStore.edit {
            it[DataStoreKeys.Host.key] = host
            if (openId?.isNotEmpty() == true){
                it[DataStoreKeys.OpenId.key] = openId
            }else
                it.remove(DataStoreKeys.OpenId.key)
        }
        mutex.withLock {
            val (client,memosApi) = createClient(host,openId)
            this.client = client
            this.memosApi = memosApi
            this.host = host
        }

        loadStatus()
    }

    suspend fun clearToken(){
        token = ""
        context.dataStore.edit {
            it.remove(DataStoreKeys.AccountToken.key)
        }
    }

    suspend fun <T> call(block: suspend (MemosApi) -> ApiResponse<T>): ApiResponse<T>{
        return memosApi?.let { block(it) } ?: ApiResponse.exception(MemosException.noLogin)
    }

//    suspend fun <T> call(block: suspend  (MemosApi) -> ApiResponse<T> ): ApiResponse<T>{
//        Timber.d("memosApi $memosApi")
//        return memosApi?.let {
//            try {
//                block(it)
//            }catch (e: Exception){
//                if (e is HttpException){
//                    val responseError = isServiceJson(e.response()?.errorBody()?.toString())
//                    val msg = if (responseError.isEmpty()) e.message() else responseError
//                    ApiResponse.create(e.code(),msg)
//                }else
//                    ApiResponse.create(-1000,"")
//            }
//
//        }?: ApiResponse.create(-1000,MemosException.noLogin.localizedMessage)
//
//    }


//    private fun isServiceJson(response: String?): String {
//        if (response.isNullOrEmpty()) return ""
//        try {
//            val json = JSONObject(response)
//            return json.getString("error")
//        } catch (e: Exception) {
//            return ""
//        }
//    }

}

