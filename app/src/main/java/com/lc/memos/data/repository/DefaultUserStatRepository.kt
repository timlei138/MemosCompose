package com.lc.memos.data.repository

import android.content.Context
import com.google.gson.Gson
import com.lc.memos.data.Status
import com.lc.memos.data.User
import com.lc.memos.data.UserRepository
import com.lc.memos.data.api.MemosApiService
import com.lc.memos.di.ApplicationScope
import com.lc.memos.di.DefaultDispatcher
import com.lc.memos.ext.JSON
import com.lc.mini.call.ApiResponse
import com.lc.mini.call.isSuccess
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUserStatRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: MemosApiService,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
): UserRepository {
    override suspend fun signInWithPassword(
        host: String,
        username: String,
        password: String
    ): ApiResponse<User> {
        val (_,client) = apiService.createClient(host)
        val params = Gson().toJson(mapOf("username" to username, "password" to password))
        try {
            val resp = client.signIn(params.toRequestBody(JSON))
            if (resp.isSuccess){
                apiService.update(host,"")

            }else{
                apiService.clearToken()
            }
            return resp
        }catch (e: Exception){
            return ApiResponse.exception(e)
        }

    }

    override suspend fun signInWithToken(host: String, token: String): ApiResponse<User> {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun me(): ApiResponse<User> = apiService.call { api ->  api.me() }
    override fun status(): Status? {
        return apiService.status
    }


}