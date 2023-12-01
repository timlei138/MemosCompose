package com.lc.memos.data.repository

import android.content.Context
import com.google.gson.Gson
import com.lc.memos.data.Status
import com.lc.memos.data.User
import com.lc.memos.data.UserRepository
import com.lc.memos.data.api.MemosApiService
import com.lc.memos.di.DefaultDispatcher
import com.lc.memos.ext.JSON
import com.lc.mini.call.ApiResponse
import com.lc.mini.call.isSuccess
import com.lc.mini.call.onError
import com.lc.mini.call.onException
import com.lc.mini.call.onFailure
import com.lc.mini.call.onSuccess
import com.lc.mini.call.retrofit.headers
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUserStatRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: MemosApiService,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
): UserRepository {
    override suspend fun signInWithPassword(
        host: String,
        username: String,
        password: String
    ): ApiResponse<User> {
        val (_, client) = apiService.createClient(host)
        val params = Gson().toJson(mapOf("username" to username, "password" to password))
        val resp = client.signIn(params.toRequestBody(JSON))
        if (resp is ApiResponse.Success){
            resp.headers["Set-Cookie"]?.also {
                Cookie.parse(host.toHttpUrl(),it)?.let { cookie ->
                    apiService.update(host,"",cookie.value)
                }
            }
        }
        return resp

    }

    override suspend fun signInWithToken(host: String, token: String): ApiResponse<User> {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun me(): ApiResponse<User> = apiService.call { api -> api.me() }
    override fun status(): Status? {
        return apiService.status
    }


}