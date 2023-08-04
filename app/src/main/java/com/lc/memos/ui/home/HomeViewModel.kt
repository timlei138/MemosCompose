package com.lc.memos.ui.home

import androidx.lifecycle.ViewModel
import com.lc.memos.data.DataRepository
import com.lc.memos.data.db.MemoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private data class HomeUiState(
    val items: List<MemoInfo> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    fun getAll(){

        repository.getAllMemoList()
    }

}