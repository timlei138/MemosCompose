package com.lc.memos.data

import com.lc.memos.data.api.Memo
import com.lc.memos.data.api.MemosApiServe
import com.lc.memos.data.api.safeCall
import com.lc.memos.data.db.MemoDao
import com.lc.memos.data.db.MemoInfo
import com.lc.memos.di.ApplicationScope
import com.lc.memos.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
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
        val list = flow<List<MemoInfo>> {

            scope.safeCall<List<Memo>> {
                call { apiSource.listMemo() }
                onSuccess {
                    val list = mutableListOf<MemoInfo>()
                    it.forEach {
                        list.add(it.toMemoInfo())
                    }
                    scope.launch {
                        emit(list)
                    }
                }
                onFailed { code, msg, e ->
                    Timber.d("onFailed $code $msg $e")
                }
            }
        }
        Timber.d("getAllMemoList")
        return list
    }


}