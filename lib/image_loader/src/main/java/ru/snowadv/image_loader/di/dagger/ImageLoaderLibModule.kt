package ru.snowadv.image_loader.di.dagger

import android.content.Context
import coil.ImageLoader
import coil.memory.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import ru.snowadv.image_loader.R


@Module
internal class ImageLoaderLibModule {
    @Provides
    @Reusable
    fun provideImageLoader(okHttpClient: OkHttpClient, appContext: Context): ImageLoader {
        return ImageLoader.Builder(appContext)
            .apply {
                memoryCache {
                    MemoryCache.Builder(appContext).maxSizePercent(0.5).build()
                }
                crossfade(true)
                error(R.drawable.bg_image_error)
                placeholder(R.drawable.bg_image_placeholder)
            }
            .okHttpClient(okHttpClient)
            .build()
    }
}