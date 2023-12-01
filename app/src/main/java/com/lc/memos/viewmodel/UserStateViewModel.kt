package com.lc.memos.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lc.memos.data.Status
import com.lc.memos.data.User
import com.lc.memos.data.UserRepository
import com.lc.memos.data.api.MemosApiService
import com.lc.memos.di.DefaultDispatcher
import com.lc.memos.ui.WhileUISubscribed
import com.lc.mini.call.ApiResponse
import com.lc.mini.call.messageOrNull
import com.lc.mini.call.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

enum class SignMethod {
    USERNAME_AND_PASSWORD,
    OPEN_API
}

data class LoginUiState(
    val loading: Boolean = false,
    val msg: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class UserStateViewModel @Inject constructor(
    private val memosApiService: MemosApiService,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
) : ViewModel() {

    var currUser: User? by mutableStateOf(User())

    val host: String get() = memosApiService.host ?: ""

    val okHttpClient: OkHttpClient  get() = memosApiService.client

    val status: Status? get() = repository.status()

    private val _uiState = MutableStateFlow(LoginUiState())

    val loginUiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    suspend fun loadCurrentUser(): ApiResponse<User> = withContext(dispatcher){
        Timber.d("loadCurrentUser")
        repository.me().suspendOnSuccess {
            currUser = data.copy(avatarIcon = getAvatarByte(data.avatarUrl))
        }
    }

    fun signIn(host: String, user: String, pwd: String) {
        if (host.isEmpty() || user.isEmpty() || pwd.isEmpty()) {
            _uiState.update { it.copy(msg = "Params Invalid") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, msg = "") }
            when (val result = repository.signInWithPassword(host, user, pwd)) {
                is ApiResponse.Success -> {
                    _uiState.update { it.copy(loading = false, success = true) }

                    currUser = result.data.copy(avatarIcon = getAvatarByte(result.data.avatarUrl))
                }
                is ApiResponse.Failure -> _uiState.update {
                    it.copy(
                        loading = false,
                        msg = result.messageOrNull
                    )
                }

                else -> _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun signInSSO(host: String, openApi: String) {

    }


    fun signOut() {

    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun getAvatarByte(iconStr: String?) = iconStr?.let {
        if (iconStr.indexOf(",") > 0)
            Base64.Default.decode(iconStr.split(",")[1])
        else
            null
    }
}

val localUserModel = compositionLocalOf<UserStateViewModel> { error("No Active User") }