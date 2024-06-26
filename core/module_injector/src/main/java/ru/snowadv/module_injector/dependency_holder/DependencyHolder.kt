package ru.snowadv.module_injector.dependency_holder

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.module_injector.module.BaseModuleDependencies

/**
 * Base interfaces for DependencyHolder.
 * These may depend on multiple APIs - that's why the componentN-like approach is used here.
 */
interface BaseDependencyHolder<D : BaseModuleDependencies> {
    val dependencies: D
}

abstract class DependencyHolder0<D : BaseModuleDependencies>(
) : BaseDependencyHolder<D> {
    abstract val block: (BaseDependencyHolder<D>) -> D

    override val dependencies: D
        get() = block(this)
}

abstract class DependencyHolder1<A1 : BaseModuleAPI, D : BaseModuleDependencies>(
    private val api1: A1,
) : BaseDependencyHolder<D> {
    abstract val block: (BaseDependencyHolder<D>, A1) -> D

    override val dependencies: D
        get() = block(this, api1)
}

abstract class DependencyHolder2<A1 : BaseModuleAPI, A2 : BaseModuleAPI, D : BaseModuleDependencies>(
    private val api1: A1,
    private val api2: A2,
) : BaseDependencyHolder<D> {
    abstract val block: (BaseDependencyHolder<D>, A1, A2) -> D

    override val dependencies: D
        get() = block(this, api1, api2)
}

abstract class DependencyHolder3<A1 : BaseModuleAPI, A2 : BaseModuleAPI, A3 : BaseModuleAPI, D : BaseModuleDependencies>(
    private val api1: A1,
    private val api2: A2,
    private val api3: A3,
) : BaseDependencyHolder<D> {
    abstract val block: (dependencyHolder: BaseDependencyHolder<D>, A1, A2, A3) -> D

    override val dependencies: D
        get() = block(this, api1, api2, api3)
}


abstract class DependencyHolder4<A1 : BaseModuleAPI, A2 : BaseModuleAPI, A3 : BaseModuleAPI, A4 : BaseModuleAPI, D : BaseModuleDependencies>(
    private val api1: A1,
    private val api2: A2,
    private val api3: A3,
    private val api4: A4,
) : BaseDependencyHolder<D> {
    abstract val block: (dependencyHolder: BaseDependencyHolder<D>, A1, A2, A3, A4) -> D

    override val dependencies: D
        get() = block(this, api1, api2, api3, api4)
}

abstract class DependencyHolder5<A1 : BaseModuleAPI, A2 : BaseModuleAPI, A3 : BaseModuleAPI,
        A4 : BaseModuleAPI, A5 : BaseModuleAPI, D : BaseModuleDependencies>(
    private val api1: A1,
    private val api2: A2,
    private val api3: A3,
    private val api4: A4,
    private val api5: A5,
) : BaseDependencyHolder<D> {
    abstract val block: (dependencyHolder: BaseDependencyHolder<D>, A1, A2, A3, A4, A5) -> D

    override val dependencies: D
        get() = block(this, api1, api2, api3, api4, api5)
}

abstract class DependencyHolder6<A1 : BaseModuleAPI, A2 : BaseModuleAPI, A3 : BaseModuleAPI,
        A4 : BaseModuleAPI, A5 : BaseModuleAPI, A6 : BaseModuleAPI, D : BaseModuleDependencies>(
    private val api1: A1,
    private val api2: A2,
    private val api3: A3,
    private val api4: A4,
    private val api5: A5,
    private val api6: A6,
) : BaseDependencyHolder<D> {
    abstract val block: (dependencyHolder: BaseDependencyHolder<D>, A1, A2, A3, A4, A5, A6) -> D

    override val dependencies: D
        get() = block(this, api1, api2, api3, api4, api5, api6)
}

abstract class DependencyHolder10<A1 : BaseModuleAPI, A2 : BaseModuleAPI, A3 : BaseModuleAPI,
        A4 : BaseModuleAPI, A5 : BaseModuleAPI, A6 : BaseModuleAPI, A7 : BaseModuleAPI, A8 : BaseModuleAPI, A9 : BaseModuleAPI, A10 : BaseModuleAPI,
        D : BaseModuleDependencies>(
    private val api1: A1,
    private val api2: A2,
    private val api3: A3,
    private val api4: A4,
    private val api5: A5,
    private val api6: A6,
    private val api7: A7,
    private val api8: A8,
    private val api9: A9,
    private val api10: A10,
) : BaseDependencyHolder<D> {
    abstract val block: (dependencyHolder: BaseDependencyHolder<D>, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) -> D

    override val dependencies: D
        get() = block(this, api1, api2, api3, api4, api5, api6, api7, api8, api9, api10)
}

abstract class DependencyHolder11<A1 : BaseModuleAPI, A2 : BaseModuleAPI, A3 : BaseModuleAPI,
        A4 : BaseModuleAPI, A5 : BaseModuleAPI, A6 : BaseModuleAPI, A7 : BaseModuleAPI, A8 : BaseModuleAPI, A9 : BaseModuleAPI, A10 : BaseModuleAPI, A11 : BaseModuleAPI,
        D : BaseModuleDependencies>(
    private val api1: A1,
    private val api2: A2,
    private val api3: A3,
    private val api4: A4,
    private val api5: A5,
    private val api6: A6,
    private val api7: A7,
    private val api8: A8,
    private val api9: A9,
    private val api10: A10,
    private val api11: A11,
) : BaseDependencyHolder<D> {
    abstract val block: (dependencyHolder: BaseDependencyHolder<D>, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) -> D

    override val dependencies: D
        get() = block(this, api1, api2, api3, api4, api5, api6, api7, api8, api9, api10, api11)
}