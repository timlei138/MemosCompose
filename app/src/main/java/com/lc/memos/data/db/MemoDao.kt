package com.lc.memos.data.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import com.lc.memos.data.api.Resource
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "notes")
data class MemosNote(
    @PrimaryKey(autoGenerate = true) var _index: Long = 0,
    @ColumnInfo(name = "noteId")var id: Long = 0,
    var pinned: Boolean = false,
    var rowStatus: String = "",
    var updatedTs: Int = 0,
    var visibility: String = "",
    var content: String = "",
    var createdTs: Int = 0,
    var creatorId: Long = 0,
    var creatorName: String = "",
    var creatorUsername: String = "",
    var displayTs: Int = 0,
    @Ignore var relationList: List<Any> = emptyList(),
    @Ignore var resourceList: List<Resource> = emptyList()
){
    constructor() : this(0)
}

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

    @Query("SELECT * FROM notes")
    fun observeAll(): Flow<List<MemosNote>>

    @Query("SELECT * FROM media")
    fun getAllResources(): Flow<List<MemoResource>>

    @Query("SELECT * FROM media WHERE creatorId=:creatorId AND noteId=:memoId")
    fun getResourceForMemo(creatorId: Long,memoId: Long): Flow<List<MemoResource>>

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

    @Query("DELETE FROM media")
    suspend fun deleteMediaAll()

    @Upsert
    suspend fun upSertMemo(memoInfo: List<MemosNote>)

}