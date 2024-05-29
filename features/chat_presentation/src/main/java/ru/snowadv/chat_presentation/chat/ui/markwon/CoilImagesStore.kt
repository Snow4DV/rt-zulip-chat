package ru.snowadv.chat_presentation.chat.ui.markwon

import android.content.Context
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.coil.CoilImagesPlugin
import javax.inject.Inject

internal class CoilImagesStore @Inject constructor(private val appContext: Context, private val imageLoader: ImageLoader) : CoilImagesPlugin.CoilStore {
    companion object {
        private const val DEFAULT_IMAGE_WIDTH_PX = 400
        private const val DEFAULT_IMAGE_HEIGHT_PX = 400
    }
    override fun load(drawable: AsyncDrawable): ImageRequest {
        return ImageRequest.Builder(appContext)
            .defaults(imageLoader.defaults)
            .size(DEFAULT_IMAGE_WIDTH_PX, DEFAULT_IMAGE_HEIGHT_PX)
            .data(drawable.destination)
            .crossfade(true)
            .build()
    }

    override fun cancel(disposable: Disposable) {
        disposable.dispose()
    }
}