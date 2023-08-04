package com.lc.memos.ui.widget

import androidx.navigation.NavHostController
import com.lc.memos.ui.widget.MemosScreens.COLLECT_SCREEN
import com.lc.memos.ui.widget.MemosScreens.EXPLORE_SCREEN
import com.lc.memos.ui.widget.MemosScreens.HOME_SCREEN
import com.lc.memos.ui.widget.MemosScreens.LOGIN_SCREEN
import com.lc.memos.ui.widget.MemosScreens.RESOURCE_SCREEN
import com.lc.memos.ui.widget.MemosScreens.SETTING_SCREEN


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
    const val HOME_ROUTE = HOME_SCREEN
    const val EXPLORE_ROUTE = EXPLORE_SCREEN
    const val RESOURCE_ROUTE = RESOURCE_SCREEN
    const val COLLECT_ROUTE = COLLECT_SCREEN
    const val SETTING_ROUTE = SETTING_SCREEN
    const val LOGIN_ROUTE = LOGIN_SCREEN
}

class MemosNavigationActions(private val navController: NavHostController){

    fun navigateToHome(){}

    fun navigateToExplore(){}

    fun navigateToResource(){}

    fun navigateToCollect(){}

    fun navigateToSetting(){}

    fun navigateToLogin(){}

}