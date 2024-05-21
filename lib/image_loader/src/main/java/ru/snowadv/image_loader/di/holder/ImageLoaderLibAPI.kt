package ru.snowadv.image_loader.di.holder

import coil.Coil
import coil.ImageLoader
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ImageLoaderLibAPI : BaseModuleAPI {
    val coilImageLoader: ImageLoader
}