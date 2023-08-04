package com.lc.memos.di

import android.content.Context
import androidx.room.Room
import com.lc.memos.data.DataRepository
import com.lc.memos.data.DefaultDataRepository
import com.lc.memos.data.api.ApiGsonConverterFactory
import com.lc.memos.data.api.MemosApiInterceptor
import com.lc.memos.data.api.MemosApiServe
import com.lc.memos.data.db.MemoDao
import com.lc.memos.data.db.MemoDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindDataRepository(repository: DefaultDataRepository): DataRepository

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
    fun providerOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder().let {
            it.readTimeout(30, TimeUnit.SECONDS)
            it.writeTimeout(30, TimeUnit.SECONDS).addInterceptor(MemosApiInterceptor())
                .addInterceptor(HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                })
            it.build()
        }
    }


    @Singleton
    @Provides
    fun providerMemoApiServe(okHttpClient: OkHttpClient): MemosApiServe {
        return Retrofit.Builder().baseUrl("http://82.156.120.42:8090").client(okHttpClient)
            .addConverterFactory(ApiGsonConverterFactory.create()).build()
            .create(MemosApiServe::class.java)
    }
}