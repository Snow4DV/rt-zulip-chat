package ru.snowadv.image_loader.di.dagger

import dagger.Component
import ru.snowadv.image_loader.di.holder.ImageLoaderLibAPI
import ru.snowadv.image_loader.di.holder.ImageLoaderLibDependencies
import ru.snowadv.module_injector.general.PerFeature

@Component(dependencies = [ImageLoaderLibDependencies::class], modules = [ImageLoaderLibModule::class])
@PerFeature
internal interface ImageLoaderLibComponent : ImageLoaderLibAPI {
    companion object {
        fun initAndGet(deps: ImageLoaderLibDependencies): ImageLoaderLibComponent {
            return DaggerImageLoaderLibComponent.builder()
                .imageLoaderLibDependencies(deps)
                .build()
        }
    }
}