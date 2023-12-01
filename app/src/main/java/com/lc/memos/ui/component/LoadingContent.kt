package com.lc.memos.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoadingContent(
    loading: Boolean,
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        val pullRefreshState =
            rememberPullRefreshState(refreshing = loading, onRefresh = onRefresh)
        Box(
            modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            content()
        }
        PullRefreshIndicator(
            refreshing = loading,
            state = pullRefreshState,
            modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,

        )
    }

}
