package com.lc.memos.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lc.memos.ui.login.LoginScreen
import com.lc.memos.ui.theme.MemosComposeTheme
import com.lc.memos.util.AppSharedPrefs.Companion.appSettings
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemosApp(widthSizeClass: WindowWidthSizeClass) {

    MemosComposeTheme {

        val navController = rememberNavController()

        val navigationActions = remember(navController) {
            MemosNavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute = navBackStackEntry?.destination?.route ?: MemosDestinations.HOME_ROUTE

        val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

        val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen = isExpandedScreen)

        var hasSigned by remember { mutableStateOf(appSettings.hasLogin()) }

        if (!hasSigned){
            LoginScreen(onSigned = {
                hasSigned = true
            })

        }else{
            ModalNavigationDrawer(drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    navigateToHome = { navigationActions.navigateToHome() },
                    navigateToExplore = { navigationActions.navigateToExplore() },
                    navigateToResource = { navigationActions.navigateToResource() },
                    navigateToCollect = { navigationActions.navigateToCollect() },
                    navigateToSetting = { navigationActions.navigateToSetting() },
                    closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } })
            }, drawerState = sizeAwareDrawerState) {
                Row {
                    if (isExpandedScreen) {

                    }

                    MemosNavGraph(
                        isExpandedScreen,
                        navController,
                        openDrawer = { coroutineScope.launch { sizeAwareDrawerState.open() } },
                        startDestination = MemosDestinations.HOME_ROUTE,
                        navigationActions
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    return if (!isExpandedScreen) {
        drawerState
    } else
        DrawerState(DrawerValue.Closed)
}