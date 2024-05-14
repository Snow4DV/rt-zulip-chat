package ru.snowadv.home_impl.di

import ru.snowadv.home_api.di.HomeFeatureAPI
import ru.snowadv.home_api.di.HomeFeatureDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object HomeFeatureComponentHolder :
    ComponentHolder<HomeFeatureAPI, HomeFeatureDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<HomeFeatureAPI, HomeFeatureDependencies, HomeFeatureComponent> {
            deps -> HomeFeatureComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> HomeFeatureDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): HomeFeatureComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): HomeFeatureAPI = componentHolderDelegate.get()
}