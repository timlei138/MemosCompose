package com.lc.memos.data.repository

import com.lc.memos.data.MemosRepository
import com.lc.memos.data.api.MemosApiService
import com.lc.memos.data.api.Resource
import com.lc.memos.data.db.MemoDao
import com.lc.memos.data.db.MemosNote
import com.lc.memos.di.ApplicationScope
import com.lc.memos.di.DefaultDispatcher
import com.lc.mini.call.ApiResponse
import com.lc.mini.call.flatMap
import com.lc.mini.call.toFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultMemosRepository @Inject constructor(
    private val apiSource: MemosApiService,
    private val localSource: MemoDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : MemosRepository {
    override fun listAllMemos(): Flow<ApiResponse<List<MemosNote>>> {
        return flow { emit(apiSource.call { api -> api.listAllMemos() }) }
    }

    override fun listAllTags(): Flow<ApiResponse<List<String>>> {
        return flow { emit(apiSource.call { api -> api.getTags() }) }
    }

    override fun listResources(): Flow<ApiResponse<List<Resource>>> {

        return flow {
            emit(
                apiSource.call {
                    withContext(dispatcher) {
                        it.getResources()
                    }
                })
        }
    }


}