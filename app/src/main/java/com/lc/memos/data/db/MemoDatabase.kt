package com.lc.memos.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MemosNote::class,MemoResource::class], version = 1, exportSchema = true)
abstract class MemoDatabase: RoomDatabase() {

    abstract fun memoDao(): MemoDao
}