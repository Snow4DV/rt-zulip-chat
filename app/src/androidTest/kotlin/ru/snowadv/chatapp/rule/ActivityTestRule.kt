package ru.snowadv.chatapp.rule

import android.app.Activity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

internal class  ActivityTestRule<T: Activity>(
    activityClass: Class<T>,
    startScreen: FragmentScreen? = null,
) : TestRule {

    val activityScenarioRule = ActivityScenarioRule(activityClass)
    val wiremockTestRule = WiremockTestRule()
    val fakeDepsRule = FakeAuthDepsInjectingRule()
    val navigationRule = CiceroneNavigationRule(startScreen)
    val mockDataRule = MockDataRule()

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.outerRule(wiremockTestRule)
            .around(activityScenarioRule)
            .around(navigationRule)
            .around(mockDataRule)
            .around(fakeDepsRule)
            .apply(base, description)
    }


}