package com.lc.memos.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.lc.memos.data.api.Memo
import com.lc.memos.ui.home.HomeViewModel
import com.lc.memos.ui.widget.LoadingContent
import com.lc.memos.ui.widget.MemosAppBar

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    isExpandedScreen: Boolean,
    openDrawer : () -> Unit,
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

        paddingValues.calculateBottomPadding()
        


    }

}

@Composable
private fun HomeContent(loading: Boolean,memos: List<Memo>,onRefresh: ()-> Unit){
    LoadingContent(loading = loading, empty = memos.isEmpty(), emptyContent = { /*TODO*/ }, onRefresh = onRefresh) {

    }
}