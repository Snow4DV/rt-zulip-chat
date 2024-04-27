package ru.snowadv.profile.di.holder

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.profile.di.dagger.ProfileFeatureComponent
import ru.snowadv.profile.di.dagger.ProfileFeatureModule

object ProfileFeatureComponentHolder : ComponentHolder<ProfileFeatureAPI, ProfileFeatureDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ProfileFeatureAPI, ProfileFeatureDependencies, ProfileFeatureComponent> {
        deps -> ProfileFeatureComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> ProfileFeatureDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ProfileFeatureComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ProfileFeatureAPI = componentHolderDelegate.get()
}