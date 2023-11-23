package com.lc.mini.call.serialization

import com.lc.mini.call.ApiResponse
import com.lc.mini.call.retrofit.errorBody
import kotlinx.serialization.json.Json

@JvmOverloads
public inline fun <T,reified E> ApiResponse<T>.deserializeErrorBody(json: Json = Json): E?{
    if (this is ApiResponse.Failure.Error){
        val errorBody = this.errorBody?.string() ?: return null
        return json.decodeFromString(errorBody)
    }
    return null
}

@JvmSynthetic
public inline fun <T,reified E> ApiResponse<T>.onErrorDeserialize(json: Json = Json,crossinline onResult: ApiResponse.Failure.Error.(E) -> Unit): ApiResponse<T>{
    val errorBody = this.deserializeErrorBody<T,E>(json = json)
    if (this is ApiResponse.Failure.Error && errorBody != null){
        onResult(errorBody)
    }
    return this
}