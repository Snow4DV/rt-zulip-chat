package ru.snowadv.chatapp.rule

import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.snowadv.chatapp.di.injector.AuthorizedTestModulesInjector

internal class FakeAuthDepsInjectingRule : TestWatcher() {
    override fun apply(base: Statement?, description: Description?): Statement {
        AuthorizedTestModulesInjector.inject(ApplicationProvider.getApplicationContext())
        return super.apply(base, description)
    }
}