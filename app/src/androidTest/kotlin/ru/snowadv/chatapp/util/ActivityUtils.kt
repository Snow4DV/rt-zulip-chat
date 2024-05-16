package ru.snowadv.chatapp.util

import android.app.Activity
import ru.snowadv.chatapp.rule.ActivityTestRule

internal object ActivityUtils {
    fun <T: Activity> ActivityTestRule<T>.onActivity(action: T.() -> Unit) {
        activityScenarioRule.scenario.onActivity {
            action(it)
        }
    }
}