package ru.snowadv.users_data_impl.di

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.users_data_api.di.UsersDataModuleAPI
import ru.snowadv.users_data_api.di.UsersDataModuleDependencies

object UsersDataModuleComponentHolder :
    ComponentHolder<UsersDataModuleAPI, UsersDataModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<UsersDataModuleAPI, UsersDataModuleDependencies, UsersDataModuleComponent> {
            deps -> UsersDataModuleComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> UsersDataModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): UsersDataModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): UsersDataModuleAPI = componentHolderDelegate.get()
}