package ru.snowadv.auth_presentation.di.holder

import ru.snowadv.auth_presentation.di.dagger.AuthPresentationComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object AuthPresentationComponentHolder :
    ComponentHolder<AuthPresentationAPI, AuthPresentationDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<AuthPresentationAPI, AuthPresentationDependencies, AuthPresentationComponent> {
            deps -> AuthPresentationComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> AuthPresentationDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): AuthPresentationComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): AuthPresentationAPI = componentHolderDelegate.get()
}