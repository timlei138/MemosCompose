package com.lc.memos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lc.memos.data.api.Resource
import com.lc.memos.data.repository.DefaultMemosRepository
import com.lc.memos.ui.WhileUISubscribed
import com.lc.mini.call.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


data class ResourceUiState(val data: List<Resource> = emptyList(), val isLoading: Boolean = false)

@HiltViewModel
class ResourceViewModel @Inject constructor(private val repository: DefaultMemosRepository) :
    ViewModel() {

    val uiState: StateFlow<ResourceUiState> = repository.listResources().map {
        if (it is ApiResponse.Success) {
            ResourceUiState(it.data)
        } else ResourceUiState(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = WhileUISubscribed,
        initialValue = ResourceUiState(isLoading = true)
    )

}