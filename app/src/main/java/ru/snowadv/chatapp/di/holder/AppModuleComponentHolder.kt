package ru.snowadv.chatapp.di.holder

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.chatapp.di.dagger.AppComponent

object AppModuleComponentHolder : ComponentHolder<AppModuleAPI, AppModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<AppModuleAPI, AppModuleDependencies, AppComponent>(
        isSingleton = true,
        componentFactory = { deps -> AppComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> AppModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): AppComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): AppModuleAPI = componentHolderDelegate.get()
}