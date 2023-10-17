package com.lc.memos.ui.home

import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lc.memos.data.db.MemoInfo
import com.lc.memos.ui.theme.MemosComposeTheme
import com.lc.memos.ui.widget.LoadingContent
import com.lc.memos.ui.widget.MemosAppBar
import com.lc.memos.util.getTimeStampFormat
import com.lc.memos.viewmodel.HomeViewModel
import io.noties.markwon.Markwon

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MemosAppBar(openDrawer = openDrawer)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Add, contentDescription = "")
            }
        }
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        HomeContent(
            loading = uiState.isLoading,
            memos = uiState.items,
            onRefresh = viewModel::refresh,
            modifier = Modifier.padding(paddingValues)
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    loading: Boolean,
    memos: List<MemoInfo>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        loading = loading,
        empty = memos.isEmpty() && !loading,
        emptyContent = { EmptyHomeContent() },
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        var queryParams by remember {
            mutableStateOf("")
        }
        Column(Modifier.fillMaxSize()) {
            LazyColumn {
                items(memos) { meminfo ->
                    MemoInfoItem(meminfo)
                }
            }
        }
    }
}


@Composable
private fun MemoInfoItem(memInfo: MemoInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = "${memInfo.createdTs.getTimeStampFormat()}", fontSize = 12.sp)
            AndroidView(factory = { context ->
                TextView(context).apply {
                    val markwon = Markwon.create(context)
                    markwon.setMarkdown(this, memInfo.content)
                }

            }, modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp))
        }

    }
}

@Composable
private fun EmptyHomeContent() {

}


@Preview
@Composable
fun PreviewHomeContent() {
    val memInfo1 =
        MemoInfo(
            noteId = 1L,
            pinned = false,
            rowStatus = "",
            updatedTs = 1111,
            visibility = "",
            content = "dddddddd",
            createdTs = 1111,
            creatorId = 1L, creatorName = "", creatorUsername = "", displayTs = 111
        )

    val debugList = arrayListOf<MemoInfo>(memInfo1)

    MemosComposeTheme {
        HomeContent(loading = false, memos = debugList, onRefresh = { /*TODO*/ })
    }

}