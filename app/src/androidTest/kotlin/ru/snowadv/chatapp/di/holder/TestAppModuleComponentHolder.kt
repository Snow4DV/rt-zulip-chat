package ru.snowadv.chatapp.di.holder

import ru.snowadv.chatapp.di.dagger.AppComponent
import ru.snowadv.chatapp.di.dagger.TestAppModuleComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object TestAppModuleComponentHolder : ComponentHolder<TestAppModuleAPI, TestAppModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<TestAppModuleAPI, TestAppModuleDependencies, TestAppModuleComponent>(
        isSingleton = true,
        componentFactory = { deps -> TestAppModuleComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> TestAppModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): TestAppModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): TestAppModuleAPI = componentHolderDelegate.get()
}