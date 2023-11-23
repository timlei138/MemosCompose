package com.lc.memos.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lc.memos.ui.page.home.HomeScreen
import com.lc.memos.ui.page.login.LoginScreen
import com.lc.memos.ui.page.settings.SettingsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemosNavGraph(
    drawerState: DrawerState,
    navigationControl: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = MemosDestinations.ROUTE_HOME,
    loginSuccess: () -> Unit,
) {

    NavHost(navController = navigationControl, startDestination = startDestination) {
        composable(MemosDestinations.ROUTE_HOME) {
            HomeScreen(openDrawer = { openDrawer() })
        }

        composable(MemosDestinations.ROUTE_EXPLORE) {

        }

        composable(MemosDestinations.ROUTE_LOGIN) {
            LoginScreen { loginSuccess() }
        }

        composable(MemosDestinations.ROUTE_SETTING){
            SettingsScreen(navigationControl)
        }
    }
}