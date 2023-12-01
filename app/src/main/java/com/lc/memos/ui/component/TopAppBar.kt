package com.lc.memos.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@ExperimentalMaterial3Api
@Composable
fun MemosAppBar(
    title: String,
    navigationIcon: ImageVector,
    openDrawer: () -> Unit,
    navigationDesc: String = "",
    actions: @Composable RowScope.() -> Unit = {},

) {

    TopAppBar(title = { Text(title) }, navigationIcon = {
        IconButton(onClick = openDrawer) {
            Icon(navigationIcon, contentDescription = navigationDesc)
        }
    }, actions = actions, modifier = Modifier)

}