package ru.snowadv.home_presentation.di.dagger

import ru.snowadv.home_presentation.di.holder.HomePresentationAPI
import ru.snowadv.home_presentation.di.holder.HomePresentationDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object HomePresentationComponentHolder :
    ComponentHolder<HomePresentationAPI, HomePresentationDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<HomePresentationAPI, HomePresentationDependencies, HomePresentationComponent> {
            deps ->
        HomePresentationComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> HomePresentationDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): HomePresentationComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): HomePresentationAPI = componentHolderDelegate.get()
}