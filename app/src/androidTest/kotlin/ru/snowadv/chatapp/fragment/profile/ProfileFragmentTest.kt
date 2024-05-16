package ru.snowadv.chatapp.fragment.profile

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.snowadv.chatapp.R
import ru.snowadv.chatapp.di.AuthorizedTestModulesInjector
import ru.snowadv.chatapp.fragment.profile.screen.ProfileFragmentScreen
import ru.snowadv.chatapp.rule.FakeAuthDepsInjectingRule
import ru.snowadv.chatapp.rule.WiremockTestRule
import ru.snowadv.people_impl.presentation.people_list.PeopleFragment
import ru.snowadv.profile_impl.presentation.profile.ProfileFragment

@RunWith(AndroidJUnit4::class)
internal class ProfileFragmentTest : TestCase() {


    @get:Rule
    val wiremockRule = WiremockTestRule()
    @get:Rule
    val fakeDepsRule = FakeAuthDepsInjectingRule()

    @Test
    fun launchMyProfile() = run {
        launchProfileFragment(null)

        flakySafely {
            step("Проверяем, что отображается профиль текущего пользователя") {
                ProfileFragmentScreen {
                    userName.hasText("Mikhail Loginov")
                    userEmail.hasText("user708835@tinkoff-android-spring-2024.zulipchat.com")
                }
            }
        }
        flakySafely {
            step("Проверяем, что отображается кнопка выхода из профиля") {
                ProfileFragmentScreen {
                    logoutButton.isVisible()
                }
            }
        }
    }

    @Test
    fun launchOtherProfile() = run {
        launchProfileFragment(1)


        flakySafely {
            step("Проверяем, что корректно отображается профиль чужого пользователя") {
                ProfileFragmentScreen {
                    userName.hasText("Mikhail Loginov")
                    userEmail.hasText("user708835@tinkoff-android-spring-2024.zulipchat.com")
                }
            }
        }
        flakySafely {
            step("Проверяем, что кнопка выхода скрыта") {
                ProfileFragmentScreen {
                    logoutButton.isGone()
                }
            }
        }
    }


    private fun launchProfileFragment(profileId: Long?): FragmentScenario<ProfileFragment> {
        return launchFragmentInContainer<ProfileFragment>(
            fragmentArgs = bundleOf(
                ProfileFragment.ARG_PROFILE_ID_KEY to profileId,
            ),
            themeResId = ru.snowadv.profile_impl.R.style.Theme_ZulipChat_Profile,
        )
    }
}