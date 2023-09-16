package com.lc.memos.data.api

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.internal.closeQuietly
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

class ApiGsonConverterFactory(val gson: Gson) : Converter.Factory() {

    companion object{
        fun create(gson: Gson = Gson()): ApiGsonConverterFactory{
            return ApiGsonConverterFactory(gson)
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(gson,adapter)
    }


    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        //TypeToken
        var targetType = TypeToken.get(type)
        if (type is ParameterizedType){
            val apiType = object : TypeToken<ApiResponse<*>>(){}.rawType
            val rawType = (type.rawType as Class<*>)
            Timber.d("parameterizedType rawType ${apiType.name},${rawType.name}")
            if (apiType.name == rawType.name){
                val childType = type.actualTypeArguments[0]
                if (childType is WildcardType){
                    targetType = TypeToken.get(childType)
                }else if (childType is Class<*>){
                    targetType = TypeToken.get(childType)
                }
            }
//            type.actualTypeArguments.forEach {
//                getRawType(it)
//                Timber.d("parameterizedType ${it} ${it is Class<*>}")
//                if (it is WildcardType){
//                    it.upperBounds.forEach {
//                        Timber.d("parameterizedType upperBounds ${it}")
//                    }
//                    it.lowerBounds.forEach {
//                        Timber.d("parameterizedType lowerBounds ${it}")
//                    }
//                }
//            }
        }
        Timber.d("targetType $targetType")
        val adapter = gson.getAdapter(targetType)
        return GsonResponseBodyConverter(gson,adapter)
    }
}


private class GsonRequestBodyConverter<T>(val gson: Gson,val adapter: TypeAdapter<T>) : Converter<T,RequestBody>{

    private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody?{
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(),Charsets.UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter,value)
        jsonWriter.close()
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }
}

private class GsonResponseBodyConverter<T>(val gson: Gson,val adapter: TypeAdapter<T>) : Converter<ResponseBody, ApiResponse<T>>{
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): ApiResponse<T> {
        try {
            val jsonReader = gson.newJsonReader(value.charStream())
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT){
                throw JsonIOException("JSON document was not fully consumed.")
            }
            return ApiResponse.create(result)
        }catch (e: Exception){
           e.printStackTrace()
           return ApiResponse.create(-1,e.message,e)
        }finally {
            value.closeQuietly()
        }
    }

}