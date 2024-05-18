package ru.snowadv.chatapp.util

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ru.snowadv.chatapp.rule.ActivityTestRule

internal object ActivityUtils {
    fun <T: Activity> ActivityTestRule<T>.onActivity(action: T.() -> Unit) {
        activityScenarioRule.scenario.onActivity {
            action(it)
        }
    }

    fun <T: FragmentActivity> ActivityTestRule<T>.onLastFragment(action: Fragment.() -> Unit) {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.supportFragmentManager.fragments.last().let {action(it) }
        }
    }
}