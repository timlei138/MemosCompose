package com.lc.memos.data

import com.google.gson.Gson
import com.lc.memos.data.api.MemosApiServe
import com.lc.memos.data.api.User
import com.lc.memos.data.api.safeApiCall
import com.lc.memos.data.db.MemoDao
import com.lc.memos.data.db.MemoInfo
import com.lc.memos.di.ApplicationScope
import com.lc.memos.di.DefaultDispatcher
import com.lc.memos.util.ApiResponse
import com.lc.memos.util.Async
import com.lc.memos.util.Failed
import com.lc.memos.util.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultDataRepository @Inject constructor(
    private val apiSource: MemosApiServe,
    private val localSource: MemoDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : DataRepository {

    private val JSON = "application/json; charset=utf-8".toMediaType()
    override fun signIn(host: String, user: String, pwd: String): Flow<User?> {
        val params = Gson().toJson(mapOf("username" to user,"password" to pwd))
        return flow<User> {
            withContext(dispatcher){
                val response = safeApiCall {
                    apiSource.signIn(params.toRequestBody(JSON))
                }
                when(response){
                    is Success -> response.data
                    is Failed -> response.error
                }
            }
        }

    }

    override fun signInSSO(host: String, openApi: String) {
        TODO("Not yet implemented")
    }

    override fun getAllMemoList(): Flow<List<MemoInfo>> {
        scope.launch(dispatcher) {
            val api = apiSource.listMemo()
            Timber.d("api $api")
        }
        Timber.d("getAllMemoList")
        return flow {  }
    }


}