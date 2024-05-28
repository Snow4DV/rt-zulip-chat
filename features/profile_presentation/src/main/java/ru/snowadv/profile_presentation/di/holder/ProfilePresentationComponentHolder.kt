package ru.snowadv.profile_presentation.di.holder

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.profile_presentation.di.dagger.ProfilePresentationComponent

object ProfilePresentationComponentHolder :
    ComponentHolder<ProfilePresentationAPI, ProfilePresentationDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ProfilePresentationAPI, ProfilePresentationDependencies, ProfilePresentationComponent> {
            deps -> ProfilePresentationComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> ProfilePresentationDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ProfilePresentationComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ProfilePresentationAPI = componentHolderDelegate.get()
}