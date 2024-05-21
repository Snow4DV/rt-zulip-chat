package ru.snowadv.auth_impl.di

import ru.snowadv.auth_api.di.AuthFeatureAPI
import ru.snowadv.auth_api.di.AuthFeatureDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object AuthFeatureComponentHolder :
    ComponentHolder<AuthFeatureAPI, AuthFeatureDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<AuthFeatureAPI, AuthFeatureDependencies, AuthFeatureComponent> {
            deps -> AuthFeatureComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> AuthFeatureDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): AuthFeatureComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): AuthFeatureAPI = componentHolderDelegate.get()
}