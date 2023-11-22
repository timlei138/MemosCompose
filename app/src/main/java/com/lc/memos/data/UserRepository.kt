package com.lc.memos.data

import androidx.annotation.Keep
import com.lc.mini.call.ApiResponse
@Keep
data class User(
    val id: Int = -1,
    val rowStatus: String = "",
    val createdTs: Long = 0L,
    val updatedTs: Long = 0L,
    val username: String = "",
    val role: String = "",
    val email: String = "",
    val nickname: String = "",
    val openId: String = "",
    val avatarUrl: String = "",
    val avatarIcon: ByteArray? = null
)

@Keep
data class Status(
    val profile: Profile,
)

data class Profile(
    val mode: String,
    val version: String
)


interface UserRepository {

    suspend fun signInWithPassword(host: String,username: String,password: String): ApiResponse<User>

    suspend fun signInWithToken(host: String,token: String): ApiResponse<User>

    fun logout()

    suspend fun me(): ApiResponse<User>
}