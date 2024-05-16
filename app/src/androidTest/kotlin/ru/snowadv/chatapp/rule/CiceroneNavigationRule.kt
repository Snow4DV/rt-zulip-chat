package ru.snowadv.chatapp.rule

import androidx.test.core.app.ApplicationProvider
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import ru.snowadv.chatapp.di.AuthorizedTestModulesInjector
import ru.snowadv.chatapp.di.holder.AppModuleComponentHolder

internal class CiceroneNavigationRule(private val startScreen: FragmentScreen? = null) : TestWatcher() {

    val router by lazy { AppModuleComponentHolder.get().router }
    val screens by lazy { AppModuleComponentHolder.get().screens }
    override fun starting(description: Description?) {
        startScreen?.let { router.newRootScreen(it) } ?: kotlin.run { router.newRootScreen(screens.Home()) }
        super.starting(description)
    }
}