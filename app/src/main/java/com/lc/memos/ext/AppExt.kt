package com.lc.memos.ext

import okhttp3.MediaType.Companion.toMediaType


val JSON = "application/json; charset=utf-8".toMediaType()
class MemosException(string: String): Exception(string){

    companion object{
        val noLogin = MemosException("NO_LOGIN")
        val invalidOpenApi = MemosException("INVALID_OPEN_API")
    }


    override fun getLocalizedMessage(): String? {
        return super.getLocalizedMessage()
    }

}