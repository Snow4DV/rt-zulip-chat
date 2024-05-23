package ru.snowadv.chatapp.rule

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.snowadv.chatapp.data.MockData
import ru.snowadv.chatapp.di.holder.TestAppModuleComponentHolder
import javax.inject.Inject

internal class MockDataRule : TestWatcher() {
    @Inject
    lateinit var data: MockData

    override fun apply(base: Statement?, description: Description?): Statement {
        TestAppModuleComponentHolder.getComponent().inject(this)
        return super.apply(base, description)
    }
}