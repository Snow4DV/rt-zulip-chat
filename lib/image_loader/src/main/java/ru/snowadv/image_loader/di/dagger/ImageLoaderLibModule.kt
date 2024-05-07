package ru.snowadv.image_loader.di.dagger

import android.content.Context
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient


@Module
internal class ImageLoaderLibModule {
    @Provides
    @Reusable
    fun imageLoader(okHttpClient: OkHttpClient, appContext: Context): ImageLoader {
        return ImageLoader.Builder(appContext)
            .okHttpClient(okHttpClient)
            .build()
    }
}