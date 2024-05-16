package ru.snowadv.chatapp.fragment.people

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.snowadv.chatapp.di.AuthorizedTestModulesInjector
import ru.snowadv.chatapp.fragment.people.screen.PeopleFragmentScreen
import ru.snowadv.chatapp.rule.FakeAuthDepsInjectingRule
import ru.snowadv.chatapp.rule.FragmentTestRule
import ru.snowadv.chatapp.rule.WiremockTestRule
import ru.snowadv.people_impl.presentation.people_list.PeopleFragment

@RunWith(AndroidJUnit4::class)
internal class PeopleFragmentTest : TestCase() {

    @get:Rule
    val fragmentTestRule = FragmentTestRule(
        fragmentClass = PeopleFragment::class.java,
        themeResId = ru.snowadv.people_impl.R.style.Theme_ZulipChat_People,
    )

    @Test
    fun peopleShowUp() = run {
        flakySafely {
            step("Проверяем, что отображаются два пользователя: `Lev` и `Богдан 2`") {
                PeopleFragmentScreen {
                    peopleRecycler.childAt<PeopleFragmentScreen.KPeopleItem>(0) {
                        userName.hasText("Lev")
                        userEmail.hasText("user709379@tinkoff-android-spring-2024.zulipchat.com")
                    }
                    peopleRecycler.childAt<PeopleFragmentScreen.KPeopleItem>(1) {
                        userName.hasText("Богдан 2")
                        userEmail.hasText("user709225@tinkoff-android-spring-2024.zulipchat.com")
                    }
                }
            }
        }
    }
}