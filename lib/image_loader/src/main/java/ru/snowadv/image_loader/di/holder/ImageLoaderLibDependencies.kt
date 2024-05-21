package ru.snowadv.image_loader.di.holder

import android.content.Context
import okhttp3.OkHttpClient
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ImageLoaderLibDependencies : BaseModuleDependencies {
    val okHttpClient: OkHttpClient
    val appContext: Context
}