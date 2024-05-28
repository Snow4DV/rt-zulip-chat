package ru.snowadv.people_presentation.di.holder

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.people_presentation.di.dagger.PeoplePresentationComponent

object PeoplePresentationComponentHolder :
    ComponentHolder<PeoplePresentationAPI, PeoplePresentationDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<PeoplePresentationAPI, PeoplePresentationDependencies, PeoplePresentationComponent> {
            deps -> PeoplePresentationComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> PeoplePresentationDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): PeoplePresentationComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): PeoplePresentationAPI = componentHolderDelegate.get()
}