package com.lc.memos.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lc.memos.ui.home.HomeScreen
import com.lc.memos.ui.login.LoginScreen
import com.lc.memos.ui.login.SettingScreen
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemosNavGraph(
    isExpandedScreen: Boolean,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = MemosDestinations.LOGIN_ROUTE,
    navigationActions: MemosNavigationActions,
    modifier: Modifier = Modifier,
) {

    NavHost(navController = navController, startDestination = startDestination,modifier = modifier){

        composable(MemosDestinations.HOME_ROUTE, arguments = listOf()){ navBackStackEntry ->
            HomeScreen(isExpandedScreen, openDrawer)
        }

        composable(MemosDestinations.SETTING_ROUTE){
            SettingScreen(signOut = {
                navigationActions.navigateToLogin()
            })
        }
    }


}