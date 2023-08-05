package com.lc.memos.data.api

import com.lc.memos.util.ApiResponse
import okhttp3.Callback
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


enum class MemoRowState(val state: String){
    VISIBILITY("NORMAL"),
    ARCHIVED("ARCHIVED")
}

enum class MemoVisibility(val state: String){
    PRIVATE("PRIVATE"),PROTECTED("PROTECTED"),PUBLIC("PUBLIC")
}

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


class MemosApiInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val url = original.url.newBuilder().let {
            it.addQueryParameter("openId","ce3c633a-36e0-4ddd-90b4-d0528100a4b8")
            it.build()
        }
        val request = original.newBuilder().let {
            it.method(original.method,original.body)
            it.url(url)
            it.build()
        }
        return chain.proceed(request)

    }

}


interface MemosApiServe {

//    @POST("/api/v1/auth/signin")
//    suspend fun signIn(@Body body: SignInInput): ApiResponse<Unit>
//
//    @POST("/api/v1/auth/signout")
//    suspend fun logout(): ApiResponse<Unit>
//
//    @GET("/api/v1/user/me")
//    suspend fun me(): ApiResponse<User>

    @GET("/api/v1/memo")
    suspend fun listMemo(
        @Query("creatorId") creatorId: Long? = null,
        @Query("rowStatus") rowStatus: MemoRowState? = null,
        @Query("visibility") visibility: MemoVisibility? = null
    ): ApiResponse<List<Memo>>

//    @POST("/api/v1/memo")
//    suspend fun createMemo(@Body body: CreateMemoInput): ApiResponse<Memo>
//
//    @GET("/api/v1/tag")
//    suspend fun getTags(@Query("creatorId") creatorId: Long? = null): ApiResponse<List<String>>
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
//    @GET("/api/v1/resource")
//    suspend fun getResources(): ApiResponse<List<Resource>>
//
//    @Multipart
//    @POST("/api/v1/resource/blob")
//    suspend fun uploadResource(@Part file: MultipartBody.Part): ApiResponse<Resource>
//
//    @DELETE("/api/v1/resource/{id}")
//    suspend fun deleteResource(@Path("id") resourceId: Long): ApiResponse<Unit>
//
//    @GET("/api/v1/status")
//    suspend fun status(): ApiResponse<Status>
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