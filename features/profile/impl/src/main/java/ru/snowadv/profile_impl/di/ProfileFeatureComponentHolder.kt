package ru.snowadv.profile_impl.di

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.profile_api.di.ProfileFeatureAPI
import ru.snowadv.profile_api.di.ProfileFeatureDependencies

object ProfileFeatureComponentHolder : ComponentHolder<ProfileFeatureAPI, ProfileFeatureDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ProfileFeatureAPI, ProfileFeatureDependencies, ProfileFeatureComponent> {
        deps -> ProfileFeatureComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> ProfileFeatureDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ProfileFeatureComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ProfileFeatureAPI = componentHolderDelegate.get()
}