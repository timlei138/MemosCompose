package com.lc.memos.di

import android.content.Context
import androidx.room.Room
import com.lc.memos.data.MemosRepository
import com.lc.memos.data.UserRepository
import com.lc.memos.data.api.MemosApiInterceptor
import com.lc.memos.data.api.MemosApiService
import com.lc.memos.data.db.MemoDao
import com.lc.memos.data.db.MemoDatabase
import com.lc.memos.data.repository.DefaultMemosRepository
import com.lc.memos.data.repository.DefaultUserStatRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepository: DefaultUserStatRepository): UserRepository

    @Singleton
    @Binds
    abstract fun bindMemosRepository(memosRepository: DefaultMemosRepository): MemosRepository

}

@Module
@InstallIn(SingletonComponent::class)
object MemoDatabaseModel {
    @Singleton
    @Provides
    fun providerMemoDatabase(@ApplicationContext context: Context): MemoDatabase {
        return Room.databaseBuilder(
            context.applicationContext, MemoDatabase::class.java, "memos.db"
        ).build()
    }

    @Provides
    fun providerMemoDao(database: MemoDatabase): MemoDao = database.memoDao()

    @Singleton
    @Provides
    fun providerMemosApiService(
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope,
        okHttpClient: OkHttpClient
    ): MemosApiService = MemosApiService(context, scope, okHttpClient)

    @Singleton
    @Provides
    fun providerOkhttpClient(@ApplicationContext context: Context): OkHttpClient {
//        val cookieManager = WebKitSyncCookieManager(
//            store = SharedPreferencesCookieStore(context, "api_cookie"),
//            cookiePolicy = CookiePolicy.ACCEPT_ALL
//        )
//        CookieManager.setDefault(cookieManager)
        return OkHttpClient.Builder().let {
            it.readTimeout(30, TimeUnit.SECONDS)
            it.writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(MemosApiInterceptor())
                .addInterceptor(HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BASIC
                })
            it.build()
        }
    }
}