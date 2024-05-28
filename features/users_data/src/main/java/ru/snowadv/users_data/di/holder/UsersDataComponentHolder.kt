package ru.snowadv.users_data.di.holder

import ru.snowadv.users_data.di.dagger.UsersDataComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object UsersDataComponentHolder :
    ComponentHolder<UsersDataAPI, UsersDataDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<UsersDataAPI, UsersDataDependencies, UsersDataComponent>(
        componentFactory = { deps -> UsersDataComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> UsersDataDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): UsersDataComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): UsersDataAPI = componentHolderDelegate.get()
}