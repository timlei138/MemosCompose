package com.lc.memos.data

import com.lc.memos.data.api.MemosApiServe
import com.lc.memos.data.db.MemoDao
import com.lc.memos.data.db.MemoInfo
import com.lc.memos.di.ApplicationScope
import com.lc.memos.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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

    override fun getAllMemoList(): Flow<List<MemoInfo>> {
        scope.launch(dispatcher) {
            val api = apiSource.listMemo()
            Timber.d("api $api")
        }
        Timber.d("getAllMemoList")
        return flow {  }
    }


}