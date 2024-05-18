package ru.snowadv.chatapp.rule

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import org.junit.rules.TestWatcher
import org.junit.runner.Description

internal class FragmentScenarioRule<T : Fragment>(
    private val fragmentClass: Class<T>,
    private val themeResId: Int,
    private val fragmentArgs: Bundle? = null,
    private val initialState: Lifecycle.State = Lifecycle.State.RESUMED,
) : TestWatcher() {
    override fun starting(description: Description?) {
        FragmentScenario.launchInContainer(fragmentClass, fragmentArgs, themeResId, initialState)
    }
}