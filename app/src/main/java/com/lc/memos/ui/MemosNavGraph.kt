package com.lc.memos.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lc.memos.ui.page.home.HomeScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemosNavGraph(drawerState: DrawerState,navigationControl: NavHostController) {

    NavHost(navController = navigationControl, startDestination = MemosDestinations.ROUTE_HOME){
        composable(MemosDestinations.ROUTE_HOME){
            HomeScreen(isExpandedScreen = false, openDrawer = { /*TODO*/ })
        }

        composable(MemosDestinations.ROUTE_EXPLORE){

        }
    }
}