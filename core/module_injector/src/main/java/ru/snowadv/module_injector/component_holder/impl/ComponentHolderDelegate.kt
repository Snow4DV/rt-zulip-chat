package ru.snowadv.module_injector.component_holder.impl

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.module_injector.util.StrongReference
import java.lang.ref.Reference
import java.lang.ref.WeakReference

/**
 * Delegate that creates component or returns existing one (depends on whether it was garbage-collected
 * or not)
 */
class ComponentHolderDelegate<A : BaseModuleAPI, D : BaseModuleDependencies, C : A>(
    private val isSingleton: Boolean = false,
    private val componentFactory: (D) -> C,
) : ComponentHolder<A, D> {

    override var dependencyProvider: (() -> D)? = null

    private var componentRef: Reference<C>? = null

    fun getComponentImpl(): C {
        var component: C? = null

        synchronized(this) {
            dependencyProvider?.let { dependencyProvider ->
                component = componentRef?.get()
                if (component == null) {
                    component = componentFactory(dependencyProvider())
                    componentRef = if (isSingleton) StrongReference(component) else WeakReference(component)
                }
            } ?: error("DependencyProvider for component with factory $componentFactory wasn't initialized")
        }

        return component
            ?: error("Component holder with factory $componentFactory was not initialized")
    }

    override fun get(): A {
        return getComponentImpl()
    }
}