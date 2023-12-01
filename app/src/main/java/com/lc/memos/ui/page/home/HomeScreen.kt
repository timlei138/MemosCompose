package com.lc.memos.ui.page.home

import android.text.format.DateUtils
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lc.memos.data.db.MemosNote
import com.lc.memos.ui.component.LoadingContent
import com.lc.memos.ui.component.MemosAppBar
import com.lc.memos.ui.component.MemosInfoCard
import com.lc.memos.ui.theme.MemosComposeTheme
import com.lc.memos.util.getTimeStampFormat
import com.lc.memos.viewmodel.MemosViewModel
import com.lc.memos.viewmodel.localMemosViewModel
import io.noties.markwon.Markwon
import timber.log.Timber

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MemosViewModel = localMemosViewModel.current,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MemosAppBar(
                title = "Memos",
                Icons.Filled.Menu,
                openDrawer = openDrawer,
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /*TODO*/ },
                text = { Text(text = "Add Memo") },
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = ""
                    )
                })
        },
    ) { paddingValues ->

        Timber.d("paddingValues ${paddingValues.calculateStartPadding(LayoutDirection.Ltr)}")

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        HomeContent(
            loading = uiState.isLoading,
            memos = uiState.items,
            onRefresh = viewModel::refresh,
            paddingValues = PaddingValues(12.dp, paddingValues.calculateTopPadding())
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    loading: Boolean,
    memos: List<MemosNote>,
    onRefresh: () -> Unit,
    paddingValues: PaddingValues
) {
    LoadingContent(
        loading = loading,
        empty = memos.isEmpty() && !loading,
        emptyContent = { EmptyHomeContent() },
        onRefresh = onRefresh,
    ) {
        var queryParams by remember {
            mutableStateOf("")
        }


        val pinned = memos.filter { it.pinned }
        val noPinned = memos.filterNot { it.pinned }

        val fullList = pinned + noPinned

        LazyColumn(
            modifier =
            Modifier.fillMaxSize(),
            contentPadding = paddingValues
        )
        {
            items(fullList) { info ->
                MemosInfoCard(note = info, head = {
                    Row {
                        Text(
                            text = DateUtils.getRelativeTimeSpanString(
                                info.createdTs * 1000,
                                System.currentTimeMillis(),
                                DateUtils.SECOND_IN_MILLIS
                            ).toString()
                        )


                    }
                }, modifier = Modifier.padding(8.dp))
            }
        }
    }
}


@Composable
private fun EmptyHomeContent() {

}

@Preview
@Composable
fun PreviewMemoList() {
    MemosComposeTheme {
        val mem = MemosNote(content = "Hello")
        val mem1 = MemosNote(content = "Hello1")
        HomeContent(
            loading = false,
            memos = arrayListOf(mem, mem1),
            onRefresh = { /*TODO*/ },
            paddingValues = PaddingValues(12.dp)
        )
    }
}