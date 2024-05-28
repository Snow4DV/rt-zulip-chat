package ru.snowadv.chatapp.di.dagger

import dagger.Component
import ru.snowadv.chatapp.ChatApp
import ru.snowadv.chatapp.activity.MainActivity
import ru.snowadv.chatapp.di.holder.AppModuleAPI
import ru.snowadv.chatapp.di.holder.AppModuleDependencies
import ru.snowadv.chatapp.di.holder.TestAppModuleAPI
import ru.snowadv.chatapp.di.holder.TestAppModuleDependencies
import ru.snowadv.chatapp.rule.CiceroneNavigationRule
import ru.snowadv.chatapp.rule.MockDataRule
import ru.snowadv.chatapp.rule.WiremockTestRule
import javax.inject.Singleton

@Component(
    dependencies = [TestAppModuleDependencies::class],
    modules = [TestAppModule::class],
)
@Singleton
internal interface TestAppModuleComponent : TestAppModuleAPI {
    fun inject(ciceroneNavigationRule: CiceroneNavigationRule)
    fun inject(mockDataRule: MockDataRule)
    fun inject(wireMockTestRule: WiremockTestRule)
    companion object {
        fun initAndGet(deps: TestAppModuleDependencies): TestAppModuleComponent {
            return DaggerTestAppModuleComponent.builder()
                .testAppModuleDependencies(deps)
                .build()
        }
    }
}