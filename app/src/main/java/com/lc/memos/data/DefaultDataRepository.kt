package com.lc.memos.data

import android.util.Base64
import com.google.gson.Gson
import com.lc.memos.data.api.MemosApiServe
import com.lc.memos.data.api.User
import com.lc.memos.data.api.safeApiCall
import com.lc.memos.data.db.MemoDao
import com.lc.memos.data.db.MemoInfo
import com.lc.memos.di.ApplicationScope
import com.lc.memos.di.DefaultDispatcher
import com.lc.memos.util.AppSharedPrefs
import com.lc.memos.data.api.Failed
import com.lc.memos.data.api.Profile
import com.lc.memos.data.api.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
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
    override suspend fun signIn(host: String, user: String, pwd: String): Async<User> {
        val params = Gson().toJson(mapOf("username" to user, "password" to pwd))
        return withContext(dispatcher) {
            val response = safeApiCall {
                apiSource.signIn(params.toRequestBody(JSON))
            }
            when (response) {
                is Success -> {
                    AppSharedPrefs.appSettings.apply {
                        updateLoginInfo(host, user, pwd)
                        updateUserInfo(response.data)
                    }
                    Async.Success(response.data)
                }

                is Failed -> Async.Error(response.code, response.msg)
            }
        }

    }

    override fun signInSSO(host: String, openApi: String) {
        TODO("Not yet implemented")
    }

    override fun getServiceState(): Flow<Profile> {
        return flow {
            val data = safeApiCall {
                apiSource.status()
            }
            val version = when(data){
                is Success -> {
                    data.data.profile
                }
                is Failed -> Profile("","")
            }
            emit(version)

        }
    }

    override fun getUserInfo(): Flow<User> {
        return flow {
            val user = AppSharedPrefs.appSettings.getUserInfo()
            val byte = if (user.avatarUrl.isNotEmpty()){
                Base64.decode(user.avatarUrl.split(",")[1],Base64.DEFAULT)
            }else{
                null
            }
            emit(user.copy(avatarIcon = byte, avatarUrl = ""))
        }
    }

    override fun getAllMemoList(): Flow<List<MemoInfo>> {
        return flow {
            val response = safeApiCall {
                apiSource.listMemo()
            }
            val data = when (response) {
                is Success -> response.data.toLocal()
                is Failed -> emptyList()
            }
            emit(data)
        }
    }

    override suspend fun refresh() {
        withContext(dispatcher){
            val response = safeApiCall {
                apiSource.listMemo()
            }

            when(response){
                is Success -> {
                    localSource.deleteAll()

                }
                is Failed -> {

                }
            }


        }
    }


}