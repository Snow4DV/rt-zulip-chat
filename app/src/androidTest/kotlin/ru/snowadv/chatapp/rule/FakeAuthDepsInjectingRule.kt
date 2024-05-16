package ru.snowadv.chatapp.rule

import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import ru.snowadv.chatapp.di.AuthorizedTestModulesInjector

internal class FakeAuthDepsInjectingRule : TestWatcher() {
    override fun starting(description: Description?) {
        AuthorizedTestModulesInjector.inject(ApplicationProvider.getApplicationContext())
        super.starting(description)
    }
}