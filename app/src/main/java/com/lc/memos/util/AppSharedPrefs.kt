package com.lc.memos.util

import android.content.Context
import android.content.SharedPreferences
import com.lc.memos.MemoApplication.Companion.appContext

class AppSharedPrefs {
    companion object{
        val appSettings by lazy {
            AppSharedPrefs()
        }
    }

    private val sharedPrefs: SharedPreferences =
        appContext.getSharedPreferences("app_settings", Context.MODE_PRIVATE)


    fun isLogin() = sharedPrefs.getString("TOKEN","")?.isNotEmpty()  ?: false



}