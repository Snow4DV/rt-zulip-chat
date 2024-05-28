package ru.snowadv.module_injector.component_holder

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.module_injector.module.BaseModuleDependencies

/**
 * Base interfaces for modules component holders.
 *
 * Each module may have API and dependencies. For example, network module can provide network service impl
 * and ask for BadAuthBehavior as dependency.
 */

interface ComponentHolder<A : BaseModuleAPI, D : BaseModuleDependencies> {
    var dependencyProvider: (() -> D)?
    fun get(): A

    val isInitialized get() = dependencyProvider != null
}