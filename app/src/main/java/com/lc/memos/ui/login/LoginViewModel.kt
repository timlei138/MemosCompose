package com.lc.memos.ui.login

import androidx.lifecycle.ViewModel
import com.lc.memos.data.DataRepository
import com.lc.memos.data.api.User
import com.lc.memos.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

enum class SignMethod {
    USERNAME_AND_PASSWORD,
    OPEN_API
}
data class LoginUiState(
    val loading: Boolean = false,
    val errorMsg: String = "",
    val user: User? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    private val _loading = MutableStateFlow(false)


    val loginUiState: MutableStateFlow<LoginUiState>
        get() = _uiState

    fun signIn(host: String, user: String, pwd: String) {
        val user = repository.signIn(host, user, pwd)
            .map { handleUser(it) }
            .catch { emit(Async.Error(-1, "")) }

    }

    fun signInSSO(host: String, openApi: String) {

    }

    fun signOut() {

    }




    private fun handleUser(user: User?): Async<User?> {
        if (user == null) {
            return Async.Error(-1, "")
        }
        return Async.Success(user)
    }
}