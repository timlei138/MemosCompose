package com.lc.memos.data.api

import com.lc.memos.util.AppSharedPrefs.Companion.appSettings
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


enum class MemoRowState(val state: String) {
    VISIBILITY("NORMAL"),
    ARCHIVED("ARCHIVED")
}

enum class MemoVisibility(val state: String) {
    PRIVATE("PRIVATE"), PROTECTED("PROTECTED"), PUBLIC("PUBLIC")
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

data class User(
    val id: Int,
    val rowStatus: String,
    val createdTs: Long,
    val updatedTs: Long,
    val username: String,
    val role: String,
    val email: String,
    val nickname: String,
    val openId: String,
    val avatarUrl: String
)

class MemosApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val url = original.url.newBuilder().let {
            val token = appSettings.getToken()
            if (token?.isNotEmpty() == true){
                it.addQueryParameter("openId",token)
            }
            it.build()
        }
        val request = original.newBuilder().let {
            it.method(original.method, original.body)
            it.url(url)
            it.build()
        }
        return chain.proceed(request)

    }

}

suspend fun <T> safeApiCall(block: suspend () -> ApiResponse<out T>): ApiResponse<out T> {
    try {
        return block.invoke()
    } catch (e: Exception) {
        if (e is HttpException){
            val responseError = isServiceJson(e.response()?.errorBody()?.string())
            val msg = if (responseError.isEmpty()) e.message() else responseError
            return ApiResponse.create(e.code(),msg)
        }
        return ApiResponse.create(-1, e.message)
    }
}



private fun isServiceJson(response: String?): String{
    if (response.isNullOrEmpty()) return ""
    try {
        val json = JSONObject(response)
        return json.getString("error")
    }catch (e: Exception){
        return ""
    }
}

interface MemosApiServe {

    @POST("/api/v1/auth/signin")
    suspend fun signIn(@Body body: RequestBody): ApiResponse<User>

    @POST("/api/v1/auth/signin/sso")
    suspend fun signInSSO(@Body body: RequestBody): ApiResponse<User>

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