package ru.snowadv.people_impl.di

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.people_api.di.PeopleFeatureAPI
import ru.snowadv.people_api.di.PeopleFeatureDependencies

object PeopleFeatureComponentHolder :
    ComponentHolder<PeopleFeatureAPI, PeopleFeatureDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<PeopleFeatureAPI, PeopleFeatureDependencies, PeopleFeatureComponent> {
            deps -> PeopleFeatureComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> PeopleFeatureDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): PeopleFeatureComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): PeopleFeatureAPI = componentHolderDelegate.get()
}