package ru.snowadv.chatapp.rule

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.chat.ui.ChatFragment
import ru.snowadv.chatapp.di.AuthorizedTestModulesInjector

internal class FragmentScenarioRule<T : Fragment>(
    private val fragmentClass: Class<T>,
    private val themeResId: Int,
    private val fragmentArgs: Bundle? = null,
    private val initialState: Lifecycle.State = Lifecycle.State.RESUMED,
) : TestWatcher() {
    override fun starting(description: Description?) {
        FragmentScenario.launchInContainer(fragmentClass, fragmentArgs, themeResId, initialState)
        super.starting(description)
    }
}