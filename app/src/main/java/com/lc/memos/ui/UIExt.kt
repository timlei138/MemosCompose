package com.lc.memos.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis = 5000L

val WhileUISubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)



@Composable
fun MemosSnackBarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackBar: @Composable (SnackbarData) -> Unit = { Snackbar(it) }
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier
            .systemBarsPadding()
            .wrapContentWidth(align = Alignment.Start)
            .widthIn(max = 550.dp), snackbar = snackBar
    )

}


@Composable
fun dpTopx(db: Dp) = with(LocalDensity.current){ db.roundToPx()}


@Composable
fun Int.string(): String{
    return stringResource(id = this)
}