package com.lc.memos.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.lc.memos.MemoApplication.Companion.appContext

class AppSharedPrefs {
    companion object{
        val appSettings by lazy {
            AppSharedPrefs()
        }
    }

    private val sharedPrefs: SharedPreferences =
        appContext.getSharedPreferences("app_settings", Context.MODE_PRIVATE)


    fun getToken() = sharedPrefs.getString("TOKEN","")

    fun updateToken(openApi: String) = sharedPrefs.edit {
        putString("TOKEN",openApi)
    }

    fun clearToken() = sharedPrefs.edit().remove("TOKEN").commit()


}