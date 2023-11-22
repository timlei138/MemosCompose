package com.lc.memos.ui

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lc.memos.ui.page.MemosPage
import com.lc.memos.ui.page.home.HomeScreen
import com.lc.memos.ui.page.login.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    isExpandedScreen: Boolean,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = MemosDestinations.ROUTE_LOGIN,
    modifier: Modifier = Modifier,
) {


    val navigationActions = remember(navController) {
        MemosNavigationActions(navController)
    }

    NavHost(navController = navController, startDestination = startDestination,modifier = modifier.background(MaterialTheme.colorScheme.surface)){

        composable(MemosDestinations.ROUTE_HOME, arguments = listOf()){ navBackStackEntry ->
            MemosPage()
        }

        composable(MemosDestinations.ROUTE_LOGIN){
            LoginScreen{
                navController.popBackStack()
                navigationActions.navigateToHome()
            }
        }

        composable(MemosDestinations.ROUTE_SETTING){
//            SettingScreen(signOut = {
//                navigationActions.navigateToLogin()
//            })
        }
    }





}