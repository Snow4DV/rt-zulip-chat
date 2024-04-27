package ru.snowadv.module_injector.module

import ru.snowadv.module_injector.dependency_holder.BaseDependencyHolder

interface BaseModuleDependencies {
    val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies>
}