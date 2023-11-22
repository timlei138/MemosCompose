@file:OptIn(ExperimentalMaterial3Api::class)

package com.lc.memos.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.lc.memos.ui.AppDrawer
import com.lc.memos.ui.MemosDestinations
import com.lc.memos.ui.MemosNavGraph
import kotlinx.coroutines.launch

@Composable
fun MemosPage() {

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    BackHandler {
        scope.launch {
            drawerState.close()
        }
    }


    ModalNavigationDrawer(drawerContent = {
        AppDrawer(
            currentRoute = MemosDestinations.ROUTE_HOME,
            navigateToHome = { /*TODO*/ },
            navigateToExplore = { /*TODO*/ },
            navigateToResource = { /*TODO*/ },
            navigateToCollect = { /*TODO*/ },
            navigateToSetting = { /*TODO*/ },
            closeDrawer = { })
    }, drawerState = drawerState) {
        MemosNavGraph(drawerState = drawerState, navigationControl = navController)
    }

}