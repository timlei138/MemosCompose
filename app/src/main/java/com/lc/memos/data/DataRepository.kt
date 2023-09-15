package com.lc.memos.data

import com.lc.memos.data.api.Memo
import com.lc.memos.data.api.User
import com.lc.memos.data.db.MemoInfo
import com.lc.memos.util.Async
import kotlinx.coroutines.flow.Flow

interface DataRepository {


    fun signIn(host: String,user: String,pwd: String): Flow<User?>

    fun signInSSO(host: String,openApi: String)

    fun getAllMemoList(): Flow<List<MemoInfo>>

}