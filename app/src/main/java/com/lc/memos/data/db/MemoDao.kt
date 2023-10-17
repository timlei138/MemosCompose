package com.lc.memos.data.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "memos")
data class MemoInfo(
    @PrimaryKey(autoGenerate = true) val _id: Long = 0,
    val noteId: Long,
    val pinned: Boolean,
    val rowStatus: String,
    val updatedTs: Int,
    val visibility: String,
    val content: String,
    val createdTs: Int,
    val creatorId: Long,
    val creatorName: String,
    val creatorUsername: String,
    val displayTs: Int,

)

@Entity(tableName = "media")
data class MemoResource(
    @PrimaryKey(autoGenerate = true) val _id: Long = 0,
    val noteId: Long,
    val resId: Long,
    val creatorId: Long,
    val createdTs: Long,
    val updatedTs: Long,
    val filename: String,
    val externalLink: String? = "",
    val type: String,
    val size: Int = 0,
    val linkedMemoAmount: Int = 1
)

@Dao
interface MemoDao {

    @Query("SELECT * FROM memos")
    fun observeAll(): Flow<List<MemoInfo>>

    @Query("SELECT * FROM media")
    fun getAllResources(): Flow<List<MemoResource>>

    @Query("SELECT * FROM media WHERE creatorId=:creatorId AND noteId=:memoId")
    fun getResourceForMemo(creatorId: Long,memoId: Long): Flow<List<MemoResource>>

    @Query("DELETE FROM MEMOS")
    suspend fun deleteAll()

    @Query("DELETE FROM media")
    suspend fun deleteMediaAll()

    @Upsert
    suspend fun upSertMemo(memoInfo: List<MemoInfo>)

}