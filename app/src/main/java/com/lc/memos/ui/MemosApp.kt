package com.lc.memos.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lc.memos.data.api.suspendOnNotLogin
import com.lc.memos.ui.component.AppDrawer
import com.lc.memos.ui.theme.MemosComposeTheme
import com.lc.memos.viewmodel.MemosViewModel
import com.lc.memos.viewmodel.UserStateViewModel
import com.lc.memos.viewmodel.localMemosViewModel
import com.lc.memos.viewmodel.localUserModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemosApp(widthSizeClass: WindowWidthSizeClass) {


    val navController = rememberNavController()

    val coroutineScope = rememberCoroutineScope()

    val navigationActions = remember(navController) {
        MemosNavigationActions(navController)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route ?: MemosDestinations.ROUTE_HOME

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen = isExpandedScreen)

    var userValid by remember { mutableStateOf(true) }

    val userModel: UserStateViewModel = hiltViewModel()

    val memosModel: MemosViewModel = hiltViewModel()

    MemosComposeTheme {

        CompositionLocalProvider(
            localMemosViewModel provides memosModel,
            localUserModel provides userModel
        ) {


            ModalNavigationDrawer(
                drawerContent = {
                    AppDrawer(
                        currentRoute = currentRoute,
                        navigateToHome = { navigationActions.navigateToHome() },
                        navigateToExplore = { navigationActions.navigateToExplore() },
                        navigateToResource = { navigationActions.navigateToResource() },
                        navigateToCollect = { navigationActions.navigateToCollect() },
                        navigateToSetting = { navigationActions.navigateToSetting() },
                        closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } })
                },
                drawerState = sizeAwareDrawerState,
                gesturesEnabled = userValid || !isExpandedScreen
            ) {

                Row {
                    if (isExpandedScreen) {

                    }

                    MemosNavGraph(
                        drawerState = sizeAwareDrawerState,
                        navigationControl = navController,
                        openDrawer = { coroutineScope.launch { sizeAwareDrawerState.open() } },
                        startDestination = currentRoute,
                        loginSuccess = {
                            userValid = true
                            navController.navigate(MemosDestinations.ROUTE_HOME){
                                popUpTo(navController.graph.startDestinationId){
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

            }

        }

        LaunchedEffect(Unit) {
            userModel.loadCurrentUser().suspendOnNotLogin {
                if (navController.currentDestination?.route != MemosDestinations.ROUTE_LOGIN) {
                    userValid = false
                    navController.navigate(MemosDestinations.ROUTE_LOGIN) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
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