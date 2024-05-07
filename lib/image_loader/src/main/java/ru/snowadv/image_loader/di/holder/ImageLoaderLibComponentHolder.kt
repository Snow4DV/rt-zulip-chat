package ru.snowadv.image_loader.di.holder

import ru.snowadv.image_loader.di.dagger.ImageLoaderLibComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object ImageLoaderLibComponentHolder : ComponentHolder<ImageLoaderLibAPI, ImageLoaderLibDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ImageLoaderLibAPI, ImageLoaderLibDependencies, ImageLoaderLibComponent> {
            deps -> ImageLoaderLibComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> ImageLoaderLibDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ImageLoaderLibComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ImageLoaderLibAPI = componentHolderDelegate.get()
}