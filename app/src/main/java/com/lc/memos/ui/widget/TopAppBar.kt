package com.lc.memos.ui.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalMaterial3Api
@Composable
fun MemosAppBar(openDrawer: () -> Unit) {

    TopAppBar(title = { Text("Memos") }, navigationIcon = {
        IconButton(onClick = openDrawer) {
            Icon(Icons.Filled.Menu, contentDescription = null)
        }
    }, modifier = Modifier)

}