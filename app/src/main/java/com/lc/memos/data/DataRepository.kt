package com.lc.memos.data

import com.lc.memos.data.api.Memo
import com.lc.memos.data.db.MemoInfo
import kotlinx.coroutines.flow.Flow

interface DataRepository {

    fun getAllMemoList(): Flow<List<MemoInfo>>

}