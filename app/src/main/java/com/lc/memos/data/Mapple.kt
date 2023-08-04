package com.lc.memos.data

import com.lc.memos.data.api.Memo
import com.lc.memos.data.db.MemoInfo

fun Memo.toMemoInfo(): MemoInfo {

    return MemoInfo(
        noteId = id,
        content = content,
        createdTs = createdTs,
        updatedTs = updatedTs,
        rowStatus = rowStatus,
        creatorId = creatorId,
        creatorName = creatorName,
        creatorUsername = creatorUsername,
        displayTs = displayTs,
        visibility = visibility,
        pinned = pinned
    )
}