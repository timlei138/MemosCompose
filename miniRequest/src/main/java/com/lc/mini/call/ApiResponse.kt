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
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.lc.mini.call

import com.lc.mini.call.annotations.InternalMiniApiAnnotation
import com.lc.mini.call.mappers.ApiResponseFailureMapper
import com.lc.mini.call.mappers.ApiResponseFailureSuspendMapper
import com.lc.mini.call.operators.ApiResponseOperator
import com.lc.mini.call.operators.ApiResponseSuspendOperator
import kotlinx.coroutines.launch

/**
 * @author skydoves (Jaewoong Eum)
 *
 * ApiResponse is an interface for constructing standard responses from the retrofit call.
 */
public sealed interface ApiResponse<out T> {

  /**
   * @author skydoves (Jaewoong Eum)
   *
   * API Success response class from OkHttp request call.
   * The [data] is a nullable generic type. (A response without data)

   * @property data The de-serialized response body of a successful data.
   * @property tag An additional value that can be held to distinguish the origin of the [data] or to facilitate post-processing of successful data.
   */
  public data class Success<T>(public val data: T, public val tag: Any? = null) : ApiResponse<T>

  /**
   * @author skydoves (Jaewoong Eum)
   *
   * API Failure response class from OkHttp request call.
   * There are two subtypes: [ApiResponse.Failure.Error] and [ApiResponse.Failure.Exception].
   */
  public sealed interface Failure<T> : ApiResponse<T> {
    /**
     * API response error case.
     * API communication conventions do not match or applications need to handle errors.
     * e.g., internal server error.
     *
     * @property payload An error payload that can contain detailed error information.
     */
    public open class Error(public val payload: Any?) : Failure<Nothing> {

      override fun equals(other: Any?): Boolean = other is Error &&
        payload == other.payload

      override fun hashCode(): Int {
        var result = 17
        result = 31 * result + payload.hashCode()
        return result
      }

      override fun toString(): String = payload.toString()
    }

    /**
     * @author skydoves (Jaewoong Eum)
     *
     * API request Exception case.
     * An unexpected exception occurs while creating requests or processing an response in the client side.
     * e.g., network connection error, timeout.
     *
     * @param throwable An throwable exception.
     *
     * @property message The localized message from the exception.
     */
    public open class Exception(public val throwable: Throwable) : Failure<Nothing> {
      public val message: String? = throwable.message

      override fun equals(other: Any?): Boolean = other is Exception &&
        throwable == other.throwable

      override fun hashCode(): Int {
        var result = 17
        result = 31 * result + throwable.hashCode()
        return result
      }

      override fun toString(): String = message.orEmpty()
    }
  }

  public companion object {
    /**
     * @author skydoves (Jaewoong Eum)
     *
     * [Failure] factory function. Only receives [Throwable] as an argument.
     *
     * @param ex A throwable.
     *
     * @return A [ApiResponse.Failure.Exception] based on the throwable.
     */
    @OptIn(InternalMiniApiAnnotation::class)
    public fun exception(ex: Throwable): Failure.Exception =
      Failure.Exception(ex).apply { operate().maps() }

    /**
     * @author skydoves (Jaewoong Eum)
     *
     * ApiResponse Factory.
     *
     * Create an [ApiResponse] from the given executable [f].
     *
     * If the [f] doesn't throw any exceptions, it creates [ApiResponse.Success].
     * If the [f] throws an exception, it creates [ApiResponse.Failure.Exception].
     */
    @OptIn(InternalMiniApiAnnotation::class)
    public inline fun <reified T> of(tag: Any? = null, crossinline f: () -> T): ApiResponse<T> {
      return try {
        val result = f()
        Success(
          data = result,
          tag = tag,
        )
      } catch (e: Exception) {
        exception(e)
      }.operate().maps()
    }

    /**
     * @author skydoves (Jaewoong Eum)
     *
     * ApiResponse Factory.
     *
     * Create an [ApiResponse] from the given executable [f].
     *
     * If the [f] doesn't throw any exceptions, it creates [ApiResponse.Success].
     * If the [f] throws an exception, it creates [ApiResponse.Failure.Exception].
     */
    @SuspensionFunction
    public suspend inline fun <reified T> suspendOf(
      tag: Any? = null,
      crossinline f: suspend () -> T,
    ): ApiResponse<T> {
      val result = f()
      return of(tag = tag) { result }
    }

    /**
     * @author skydoves (Jaewoong Eum)
     *
     * Operates if there is a global [com.skydoves.sandwich.operators.SandwichOperator]
     * which operates on [ApiResponse]s globally on each response and returns the target [ApiResponse].
     *
     * @return [ApiResponse] A target [ApiResponse].
     */
    @InternalMiniApiAnnotation
    @Suppress("UNCHECKED_CAST")
    public fun <T> ApiResponse<T>.operate(): ApiResponse<T> = apply {
      val globalOperators = SandwichInitializer.sandwichOperators
      globalOperators.forEach { globalOperator ->
        if (globalOperator is ApiResponseOperator<*>) {
          operator(globalOperator as ApiResponseOperator<T>)
        } else if (globalOperator is ApiResponseSuspendOperator<*>) {
          val scope = SandwichInitializer.sandwichScope
          scope.launch {
            suspendOperator(globalOperator as ApiResponseSuspendOperator<T>)
          }
        }
      }
    }

    @InternalMiniApiAnnotation
    @Suppress("UNCHECKED_CAST")
    public fun <T> ApiResponse<T>.maps(): ApiResponse<T> {
      val mappers = SandwichInitializer.sandwichFailureMappers
      var response: ApiResponse<T> = this
      mappers.forEach { mapper ->
        if (response is Failure) {
          if (mapper is ApiResponseFailureMapper) {
            response = mapper.map(response as Failure<T>) as ApiResponse<T>
          } else if (mapper is ApiResponseFailureSuspendMapper) {
            val scope = SandwichInitializer.sandwichScope
            scope.launch {
              response = mapper.map(response as Failure<T>) as ApiResponse<T>
            }
          }
        }
      }
      return response
    }
  }
}
