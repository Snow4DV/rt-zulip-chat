package ru.snowadv.chatapp.rule

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.snowadv.chatapp.R

internal class  FragmentTestRule<T: Fragment>(
    fragmentClass: Class<T>? = null,
    themeResId: Int = androidx.fragment.testing.manifest.R.style.FragmentScenarioEmptyFragmentActivityTheme,
    fragmentArgs: Bundle? = null,
    initialState: Lifecycle.State = Lifecycle.State.RESUMED,
) : TestRule {

    val wiremockRule = WiremockTestRule()
    val fakeDepsRule = FakeAuthDepsInjectingRule()
    val fragmentScenarioRule = fragmentClass?.let {
        FragmentScenarioRule(
            fragmentClass = fragmentClass,
            themeResId = themeResId,
            fragmentArgs = fragmentArgs,
            initialState = initialState,
        )
    }
    val mockDataRule = MockDataRule()

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.outerRule(wiremockRule)
            .let { fragmentScenarioRule?.let { fragmentScenarioRule -> it.around(fragmentScenarioRule) } ?: it }
            .around(mockDataRule)
            .around(fakeDepsRule)
            .apply(base, description)
    }
}