package com.lc.memos.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.lc.memos.ui.MemosScreens.COLLECT_SCREEN
import com.lc.memos.ui.MemosScreens.EXPLORE_SCREEN
import com.lc.memos.ui.MemosScreens.HOME_SCREEN
import com.lc.memos.ui.MemosScreens.LOGIN_SCREEN
import com.lc.memos.ui.MemosScreens.RESOURCE_SCREEN
import com.lc.memos.ui.MemosScreens.SETTING_SCREEN
import timber.log.Timber


private object MemosScreens{
    //主页
    const val HOME_SCREEN = "home"
    //探索页
    const val EXPLORE_SCREEN = "explore"
    //资源页
    const val RESOURCE_SCREEN = "resource"
    //归档页
    const val COLLECT_SCREEN = "collect"
    //设置页
    const val SETTING_SCREEN = "settings"
    //登录页
    const val LOGIN_SCREEN = "login"
}

object MemosDestinations{
    const val ROUTE_HOME = HOME_SCREEN
    const val ROUTE_EXPLORE = EXPLORE_SCREEN
    const val ROUTE_RESOURCE = RESOURCE_SCREEN
    const val ROUTE_COLLECT = COLLECT_SCREEN
    const val ROUTE_SETTING = SETTING_SCREEN
    const val ROUTE_LOGIN = LOGIN_SCREEN
}

class MemosNavigationActions(private val navController: NavHostController){

    fun navigateToHome(){
        navController.navigate(HOME_SCREEN){
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToExplore(){
        navController.navigate(EXPLORE_SCREEN)
    }

    fun navigateToResource(){
        navController.navigate(RESOURCE_SCREEN){
            restoreState = true
            launchSingleTop = true
        }
    }

    fun navigateToCollect(){
        navController.navigate(COLLECT_SCREEN)
    }

    fun navigateToSetting(){
        Timber.d("navigateToSetting")
        navController.navigate(SETTING_SCREEN){
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToLogin(){
        navController.navigate(MemosDestinations.ROUTE_LOGIN) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

}