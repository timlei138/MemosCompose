package com.lc.memos.data

import com.lc.memos.data.api.User
import com.lc.memos.data.db.MemoInfo
import kotlinx.coroutines.flow.Flow

interface DataRepository {


    suspend fun signIn(host: String,user: String,pwd: String): Async<User>

    fun signInSSO(host: String,openApi: String)

    fun getAllMemoList(): Flow<List<MemoInfo>>

}