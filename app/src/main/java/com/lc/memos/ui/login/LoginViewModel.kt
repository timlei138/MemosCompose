package com.lc.memos.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lc.memos.data.DataRepository
import com.lc.memos.data.api.User
import com.lc.memos.data.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

enum class SignMethod {
    USERNAME_AND_PASSWORD,
    OPEN_API
}

data class LoginUiState(
    val loading: Boolean = false,
    val msg: String? = null,
    val user: User? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {


    private val _uiState = MutableStateFlow(LoginUiState())


    val loginUiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun signIn(host: String, user: String, pwd: String) {
        if (host.isEmpty() || user.isEmpty() || pwd.isEmpty()){
            _uiState.update { it.copy(msg = "Params Invalid") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result = repository.signIn(host, user, pwd)
            Timber.d("result-> $result")
            when(result){
                is Async.Success -> _uiState.update { it.copy(loading = false, user = result.data) }
                is Async.Error -> _uiState.update { it.copy(loading = false, msg = result.errorMsg) }
                else -> _uiState.update { it.copy(loading = false) }
            }
        }



    }

    fun signInSSO(host: String, openApi: String) {

    }

    fun signOut() {

    }
}