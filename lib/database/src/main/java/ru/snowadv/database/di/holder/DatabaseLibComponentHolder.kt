package ru.snowadv.database.di.holder

import ru.snowadv.database.di.dagger.DatabaseLibComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object DatabaseLibComponentHolder :
    ComponentHolder<DatabaseLibAPI, DatabaseLibDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<DatabaseLibAPI, DatabaseLibDependencies, DatabaseLibComponent>(
        isSingleton = true,
        componentFactory = { deps -> DatabaseLibComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> DatabaseLibDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): DatabaseLibComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): DatabaseLibAPI = componentHolderDelegate.get()
}