package com.lc.memos.ext

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")
sealed class DataStoreKeys<T> {

    abstract val key: Preferences.Key<T>

    object Host: DataStoreKeys<String>(){
        override val key: Preferences.Key<String>
            get() = stringPreferencesKey("host")

    }

    object OpenId: DataStoreKeys<String>(){
        override val key: Preferences.Key<String>
            get() = stringPreferencesKey("openId")

    }

    object AccountToken: DataStoreKeys<String>(){
        override val key: Preferences.Key<String>
            get() = stringPreferencesKey("token")

    }
}