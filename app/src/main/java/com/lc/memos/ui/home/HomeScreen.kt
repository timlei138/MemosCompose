package com.lc.memos.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.lc.memos.ui.home.HomeViewModel
import com.lc.memos.ui.widget.MemosAppBar

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    isExpandedScreen: Boolean,
    openDrawer : () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {


    Scaffold(
        modifier.fillMaxSize(),
        topBar = {
            MemosAppBar(openDrawer = openDrawer)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {

            }
        }
    ) { paddingValues ->
        paddingValues.calculateBottomPadding()
        viewModel.getAll()
        Text(text = "Hello")
    }

}