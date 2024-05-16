package ru.snowadv.chatapp.rule

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.ActivityTestRule
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.junit.BeforeClass
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.snowadv.chatapp.ChatApp
import ru.snowadv.chatapp.activity.MainActivity
import ru.snowadv.chatapp.di.AuthorizedTestModulesInjector
import ru.snowadv.chatapp.glue.injector.ReleaseModulesInjector

internal class  ActivityTestRule<T: Activity>(
    activityClass: Class<T>,
    startScreen: FragmentScreen? = null,
) : TestRule {

    val activityScenarioRule = ActivityScenarioRule(activityClass)
    val wiremockTestRule = WiremockTestRule()
    private val fakeDepsRule = FakeAuthDepsInjectingRule()
    val navigationRule = CiceroneNavigationRule(startScreen)

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.outerRule(wiremockTestRule)
            .around(fakeDepsRule)
            .around(activityScenarioRule)
            .around(navigationRule)
            .apply(base, description)
    }


}