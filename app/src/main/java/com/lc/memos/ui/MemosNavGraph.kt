package com.lc.memos.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lc.memos.ui.home.HomeScreen
import com.lc.memos.ui.widget.MemosDestinations
import com.lc.memos.ui.widget.MemosNavigationActions
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemosNavGraph(
    isExpandedScreen: Boolean,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = MemosDestinations.HOME_ROUTE,
    modifier: Modifier = Modifier,
) {

    NavHost(navController = navController, startDestination = startDestination,modifier = modifier){

        composable(MemosDestinations.HOME_ROUTE, arguments = listOf()){ navBackStackEntry ->
            HomeScreen(isExpandedScreen, openDrawer)
        }
    }


}