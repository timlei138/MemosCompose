/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lc.mini.call.retrofit.adapters.internal

import com.lc.mini.call.ApiResponse
import com.lc.mini.call.retrofit.responseOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

/**
 * @author skydoves (Jaewoong Eum)
 *
 * ApiResponseCallDelegate is a delegate [Call] proxy for handling and transforming normal generic type [T]
 * as [ApiResponse] that wrapping [T] data from the network responses.
 */
internal class ApiResponseCallDelegate<T>(
  proxy: Call<T>,
  private val coroutineScope: CoroutineScope,
) : CallDelegate<T, ApiResponse<T>>(proxy) {

  override fun enqueueImpl(callback: Callback<ApiResponse<T>>) {
    coroutineScope.launch {
      try {
        val response = proxy.awaitResponse()
        val apiResponse = ApiResponse.responseOf { response }
        callback.onResponse(this@ApiResponseCallDelegate, Response.success(apiResponse))
      } catch (e: Exception) {
        callback.onResponse(
          this@ApiResponseCallDelegate,
          Response.success(ApiResponse.exception(e)),
        )
      }
    }
  }

  override fun executeImpl(): Response<ApiResponse<T>> =
    runBlocking(coroutineScope.coroutineContext) {
      val response = proxy.execute()
      val apiResponse = ApiResponse.responseOf { response }
      Response.success(apiResponse)
    }

  override fun cloneImpl() = ApiResponseCallDelegate(proxy.clone(), coroutineScope)
}
