package com.lc.memos.ext

import coil.ImageLoader
import com.lc.memos.MemoApplication.Companion.appContext
import io.noties.markwon.image.coil.CoilImagesPlugin
import javax.inject.Singleton

val coilPlug: CoilImagesPlugin
    get() = CoilImagesPlugin.create(appContext, imageLoader)


private val imageLoader: ImageLoader
    get() = ImageLoader.Builder(appContext).apply {

    }.build()

