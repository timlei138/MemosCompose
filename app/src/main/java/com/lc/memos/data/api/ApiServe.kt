package com.lc.memos.data.api

import android.net.Uri
import com.lc.memos.data.Status
import com.lc.memos.data.User
import com.lc.memos.data.db.MemosNote
import com.lc.mini.call.ApiResponse
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response

import retrofit2.http.Body
import retrofit2.http.GET

import retrofit2.http.POST
import retrofit2.http.Query


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
) {
    fun toFileUri(host: String): Uri {
        if (externalLink.isNotEmpty()) {
            return Uri.parse(externalLink)
        }
        return Uri.parse(host).buildUpon().appendPath("o").appendPath("r").appendPath(id.toString())
            .appendPath(filename).build()
    }

    fun toThumbnailFileUri(host: String, size: Int = 1): Uri {
        if (externalLink.isNotEmpty()) {
            return Uri.parse(externalLink)
        }
        return Uri.parse(host).buildUpon().appendPath("o").appendPath("r").appendPath(id.toString())
            .appendQueryParameter("thumbnail", "$size").build()
    }
}

enum class MemoRowState(val state: String) {
    VISIBILITY("NORMAL"),
    ARCHIVED("ARCHIVED")
}

enum class MemoVisibility(val state: String) {
    PRIVATE("PRIVATE"), PROTECTED("PROTECTED"), PUBLIC("PUBLIC")
}

class MemosApiInterceptor : Interceptor {
    var token = ""
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder().let {
            it.method(original.method, original.body)
            it.headers(
                okhttp3.Headers.Builder().add("Authorization", "Bearer $token").build()
            )
            it.build()
        }
        return chain.proceed(request)

    }
}

interface MemosApi {

    @POST("/api/v1/auth/signin")
    suspend fun signIn(@Body body: RequestBody): ApiResponse<User>

    @POST("/api/v1/auth/signin/sso")
    suspend fun signInSSO(@Body body: RequestBody): ApiResponse<User>

    @GET("/api/v1/status")
    suspend fun status(): ApiResponse<Status>

    @GET("/api/v1/user/me")
    suspend fun me(): ApiResponse<User>

    @POST("/api/v1/auth/signout")
    suspend fun logout(): ApiResponse<Unit>

    @GET("/api/v1/memo")
    suspend fun listAllMemos(
        @Query("creatorId") creatorId: Long? = null,
        @Query("rowStatus") rowStatus: MemoRowState? = null,
        @Query("visibility") visibility: MemoVisibility? = null,
    ): ApiResponse<List<MemosNote>>


    @GET("/api/v1/tag")
    suspend fun getTags(@Query("creatorId") creatorId: Long? = null): ApiResponse<List<String>>

    //    @POST("/api/v1/memo")
//    suspend fun createMemo(@Body body: CreateMemoInput): ApiResponse<Memo>
//
//
//    @POST("/api/v1/tag")
//    suspend fun updateTag(@Body body: UpdateTagInput): ApiResponse<String>
//
//    @POST("/api/v1/memo/{id}/organizer")
//    suspend fun updateMemoOrganizer(@Path("id") memoId: Long, @Body body: UpdateMemoOrganizerInput): ApiResponse<Memo>
//
//    @PATCH("/api/v1/memo/{id}")
//    suspend fun patchMemo(@Path("id") memoId: Long, @Body body: PatchMemoInput): ApiResponse<Memo>
//
//    @DELETE("/api/v1/memo/{id}")
//    suspend fun deleteMemo(@Path("id") memoId: Long): ApiResponse<Unit>
//
    @GET("/api/v1/resource")
    suspend fun getResources(): ApiResponse<List<Resource>>
//
//    @Multipart
//    @POST("/api/v1/resource/blob")
//    suspend fun uploadResource(@Part file: MultipartBody.Part): ApiResponse<Resource>
//
//    @DELETE("/api/v1/resource/{id}")
//    suspend fun deleteResource(@Path("id") resourceId: Long): ApiResponse<Unit>
//

//
//    @GET("/api/v1/memo/all")
//    suspend fun listAllMemo(
//        @Query("pinned") pinned: Boolean? = null,
//        @Query("tag") tag: String? = null,
//        @Query("visibility") visibility: MemosVisibility? = null,
//        @Query("limit") limit: Int? = null,
//        @Query("offset") offset: Int? = null
//    ): ApiResponse<List<Memo>>
}