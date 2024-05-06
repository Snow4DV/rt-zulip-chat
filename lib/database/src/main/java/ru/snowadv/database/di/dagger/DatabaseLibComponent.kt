package ru.snowadv.database.di.dagger

import dagger.Component
import ru.snowadv.database.di.holder.DatabaseLibAPI
import ru.snowadv.database.di.holder.DatabaseLibDependencies
import ru.snowadv.module_injector.general.PerFeature
import javax.inject.Singleton

@Component(dependencies = [DatabaseLibDependencies::class], modules = [DatabaseLibModule::class])
@Singleton
internal interface DatabaseLibComponent : DatabaseLibAPI {
    companion object {
        fun initAndGet(deps: DatabaseLibDependencies): DatabaseLibComponent {
            return DaggerDatabaseLibComponent.builder()
                .databaseLibDependencies(deps)
                .build()
        }
    }
}