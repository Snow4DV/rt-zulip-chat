package ru.snowadv.chatapp.fragment.people

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.snowadv.chatapp.fragment.people.screen.PeopleFragmentScreen
import ru.snowadv.chatapp.rule.FragmentTestRule
import ru.snowadv.people_impl.presentation.people_list.PeopleFragment

@RunWith(AndroidJUnit4::class)
internal class PeopleFragmentTest : TestCase() {

    @get:Rule
    val fragmentTestRule = FragmentTestRule(
        fragmentClass = PeopleFragment::class.java,
        themeResId = ru.snowadv.people_impl.R.style.Theme_ZulipChat_People,
    )

    private val mockData get() = fragmentTestRule.mockDataRule.data

    @Test
    fun peopleShowUp() = run {
        flakySafely(intervalMs = 200) {
            val expectedUsers = mockData.peopleDto.users
            step("Проверяем, что отображаются пользователи $expectedUsers") {
                PeopleFragmentScreen {
                    expectedUsers.forEachIndexed { index, userResponseDto ->
                        peopleRecycler.childAt<PeopleFragmentScreen.KPeopleItem>(index) {
                            userName.hasText(userResponseDto.name)
                            userEmail.hasText(userResponseDto.email)
                        }
                    }
                }
            }
        }
    }
}