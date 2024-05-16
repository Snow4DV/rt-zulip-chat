package ru.snowadv.chatapp.rule

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

internal class  FragmentTestRule<T: Fragment>(
    fragmentClass: Class<T>,
    themeResId: Int,
    fragmentArgs: Bundle? = null,
    initialState: Lifecycle.State = Lifecycle.State.RESUMED,
) : TestRule {

    val wiremockRule = WiremockTestRule()
    private val fakeDepsRule = FakeAuthDepsInjectingRule()
    val fragmentScenarioRule = FragmentScenarioRule(
        fragmentClass = fragmentClass,
        themeResId = themeResId,
        fragmentArgs = fragmentArgs,
        initialState = initialState,
    )

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.outerRule(wiremockRule)
            .around(fakeDepsRule)
            .around(fragmentScenarioRule)
            .apply(base, description)
    }
}