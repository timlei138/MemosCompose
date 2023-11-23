package com.lc.memos

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import kotlin.properties.Delegates

@HiltAndroidApp
class MemoApplication : Application() {
    companion object {
        var appContext by Delegates.notNull<Context>()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Timber.plant(Timber.DebugTree())
    }

}