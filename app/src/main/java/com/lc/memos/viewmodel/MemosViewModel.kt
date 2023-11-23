package com.lc.memos.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lc.memos.data.DailyUsageStat
import com.lc.memos.data.db.MemosNote
import com.lc.memos.data.repository.DefaultMemosRepository
import com.lc.memos.ui.WhileUISubscribed
import com.lc.mini.call.ApiResponse
import com.lc.mini.call.getOrElse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class HomeUiState(
    val items: List<MemosNote> = emptyList(),
    val isLoading: Boolean = false,
    val queryParams: String? = "",
    val dayUsage: List<DailyUsageStat> = emptyList(),
    val tags: List<String> = emptyList(),
    val msg: String? = null
)

@HiltViewModel
class MemosViewModel @Inject constructor(private val repository: DefaultMemosRepository) : ViewModel() {

    private val _queryFilter = MutableStateFlow("")

    private val _isLoading = MutableStateFlow(false)

    private val dayUsage = MutableStateFlow(DailyUsageStat.initMatrix)

    private val _filterAsync =
        combine(repository.listAllMemos(), _queryFilter) { memos, filter ->
            filterMemoList(memos, filter)
        }.catch { emit(emptyList()) }


    val uiState: StateFlow<HomeUiState> =
        combine(_isLoading, _filterAsync,dayUsage,repository.listAllTags()) { isLoading, asyncMemos,dayUsage,tags ->
            Timber.d("asyncMemos $tags")
            HomeUiState(isLoading = false, items = asyncMemos,dayUsage = dayUsage, tags = tags.getOrElse(emptyList()
            ))
        }.stateIn(viewModelScope, WhileUISubscribed, initialValue = HomeUiState(isLoading = true))


    fun refresh() {
        Timber.d("refresh.....")
        _isLoading.value = true
        viewModelScope.launch {
            _isLoading.value = false
        }
    }


    private fun filterMemoList(result: ApiResponse<List<MemosNote>>, queryParams: String): List<MemosNote> {
        return when(result){
            is ApiResponse.Success -> result.data.filter { it.content.contains(queryParams) }
            else -> return emptyList()
        }
    }

}

val localMemosViewModel = compositionLocalOf<MemosViewModel> { error("No Active User") }