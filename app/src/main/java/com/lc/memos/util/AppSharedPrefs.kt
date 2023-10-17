package com.lc.memos.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.lc.memos.MemoApplication.Companion.appContext
import com.lc.memos.data.api.User

class AppSharedPrefs {
    companion object{
        val appSettings by lazy {
            AppSharedPrefs()
        }
    }

    private val sharedPrefs: SharedPreferences =
        appContext.getSharedPreferences("app_settings", Context.MODE_PRIVATE)


    fun updateLoginInfo(host: String,user: String, pwd: String){
        sharedPrefs.edit {
            putString("host",host)
            putString("user",user)
            putString("pwd",pwd)
        }
    }

    fun hasLogin() = sharedPrefs.getString("user","")?.isNotEmpty() ?: false


    fun updateUserInfo(user: User){
        sharedPrefs.edit {
            putInt("id",user.id)
            putString("rowStatus",user.rowStatus)
            putLong("createdTs",user.createdTs)
            putLong("updatedTs",user.updatedTs)
            putString("username",user.username)
            putString("role",user.role)
            putString("email",user.email)
            putString("nickname",user.nickname)
            putString("avatarUrl",user.avatarUrl)
        }
    }

    fun getUserInfo(): User {
        val id = sharedPrefs.getInt("id",-1)
        val rowState = sharedPrefs.getString("rowStatus","") ?: ""
        val createTs = sharedPrefs.getLong("createdTs",0L)
        val updateTs = sharedPrefs.getLong("updatedTs",0L)
        val username = sharedPrefs.getString("username","") ?: ""
        val role = sharedPrefs.getString("role","") ?: ""
        val email = sharedPrefs.getString("email","") ?: ""
        val nickname = sharedPrefs.getString("nickname","") ?: ""
        val openId = sharedPrefs.getString("openId","") ?: ""
        val avatarUrl = sharedPrefs.getString("avatarUrl","") ?: ""
        return User(id,rowState,createTs,updateTs,username,role,email,nickname,openId,avatarUrl)
    }

    fun clearToken() = sharedPrefs.edit().remove("openId").commit()


}