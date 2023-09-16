package com.lc.memos.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lc.memos.data.DataRepository
import com.lc.memos.data.db.MemoInfo
import com.lc.memos.ui.widget.WhileUISubscribed
import com.lc.memos.data.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class HomeUiState(
    val items: List<MemoInfo> = emptyList(),
    val isLoading: Boolean = false,
    val queryParams: String? = "",
    val msg: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    private val _queryFilter = MutableStateFlow("")

    private val _isLoading = MutableStateFlow(false)

    private val _filterAsync =
        combine(repository.getAllMemoList(), _queryFilter) { memoInfos, filter ->
            filterMemoList(memoInfos, filter)
        }.map { Async.Success(it) }.catch<Async<List<MemoInfo>>> { emit(Async.Error(-1, "")) }


    val uiState: StateFlow<HomeUiState> =
        combine(_isLoading, _filterAsync) { isLoading, asyncMemos ->
            Timber.d("asyncMemos $asyncMemos")
            when (asyncMemos) {
                is Async.Loading -> HomeUiState(isLoading = true)
                is Async.Success -> HomeUiState(
                    asyncMemos.data,
                    isLoading = isLoading,
                    queryParams = ""
                )

                is Async.Error -> HomeUiState(msg = asyncMemos.errorMsg)
            }
        }.stateIn(viewModelScope, WhileUISubscribed, initialValue = HomeUiState(isLoading = true))


    fun refresh() {
        Timber.d("refresh.....")
        _isLoading.value = true
        viewModelScope.launch {
            _isLoading.value = false
        }
    }


    private fun filterMemoList(memoList: List<MemoInfo>, queryParams: String): List<MemoInfo> {
        if (queryParams.isEmpty()) return memoList
        return memoList.filter { it.content.contains(queryParams) }
    }

}