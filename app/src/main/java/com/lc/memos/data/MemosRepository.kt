package com.lc.memos.data

import com.lc.memos.data.api.Resource
import com.lc.memos.data.db.MemosNote
import com.lc.mini.call.ApiResponse
import kotlinx.coroutines.flow.Flow

interface MemosRepository {
    fun listAllMemos(): Flow<ApiResponse<List<MemosNote>>>

    fun listAllTags(): Flow<ApiResponse<List<String>>>

    fun listResources(): Flow<ApiResponse<List<Resource>>>

}