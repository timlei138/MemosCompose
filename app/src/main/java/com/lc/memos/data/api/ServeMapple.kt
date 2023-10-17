package com.lc.memos.data.api
data class Memo(
    val content: String,
    val createdTs: Int,
    val creatorId: Long,
    val creatorName: String,
    val creatorUsername: String,
    val displayTs: Int,
    val id: Long,
    val pinned: Boolean,
    val relationList: List<Any>,
    val resourceList: List<Resource>,
    val rowStatus: String,
    val updatedTs: Int,
    val visibility: String
)

data class Resource(
    val createdTs: Int,
    val creatorId: Int,
    val externalLink: String,
    val filename: String,
    val id: Int,
    val linkedMemoAmount: Int,
    val size: Int,
    val type: String,
    val updatedTs: Int
)

data class User(
    val id: Int = -1,
    val rowStatus: String = "",
    val createdTs: Long = 0L,
    val updatedTs: Long = 0L,
    val username: String = "",
    val role: String = "",
    val email: String = "",
    val nickname: String = "",
    val openId: String = "",
    val avatarUrl: String = "",
    val avatarIcon: ByteArray? = null
)


data class Status(
    val additionalScript: String,
    val additionalStyle: String,
    val allowSignUp: Boolean,
    val autoBackupInterval: Int,
    val customizedProfile: CustomizedProfile,
    val dbSize: Int,
    val disablePasswordLogin: Boolean,
    val disablePublicMemos: Boolean,
    val host: Host,
    val localStoragePath: String,
    val maxUploadSizeMiB: Int,
    val memoDisplayWithUpdatedTs: Boolean,
    val profile: Profile,
    val storageServiceId: Int
)

data class Profile(
    val mode: String,
    val version: String
)

data class Host(
    val avatarUrl: String,
    val createdTs: Int,
    val email: String,
    val id: Int,
    val nickname: String,
    val role: String,
    val rowStatus: String,
    val updatedTs: Int,
    val userSettingList: Any?,
    val username: String
)

data class CustomizedProfile(
    val appearance: String,
    val description: String,
    val externalUrl: String,
    val locale: String,
    val logoUrl: String,
    val name: String
)