package ru.snowadv.emojis_data_impl.di

import dagger.Component
import ru.snowadv.emojis_data_api.model.di.EmojisDataModuleAPI
import ru.snowadv.emojis_data_api.model.di.EmojisDataModuleDependencies
import javax.inject.Singleton

@Singleton
@Component(dependencies = [EmojisDataModuleDependencies::class], modules = [EmojisDataModule::class])
internal interface EmojisDataModuleComponent : EmojisDataModuleAPI {
    companion object {
        fun initAndGet(deps: EmojisDataModuleDependencies): EmojisDataModuleComponent {
            return DaggerEmojisDataModuleComponent.builder()
                .emojisDataModuleDependencies(deps)
                .build()
        }
    }
}