package ru.snowadv.chatapp.fragment.channels

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.snowadv.channels_impl.presentation.channel_list.ChannelListFragment
import ru.snowadv.chatapp.di.AuthorizedTestModulesInjector
import ru.snowadv.chatapp.rule.WiremockTestRule
import ru.snowadv.chatapp.fragment.channels.screen.ChannelsFragmentScreen

@RunWith(AndroidJUnit4::class)
internal class ChannelsFragmentTest : TestCase() {


    @get:Rule
    val wiremockRule = WiremockTestRule()

    @Test
    fun streamsShowUp() = run {
        AuthorizedTestModulesInjector.inject(ApplicationProvider.getApplicationContext())

        val scenario = launchChannelsFragment()

        ChannelsFragmentScreen {
            step("Проверяем, что стрим с подпиской отображается") {
                flakySafely {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.firstChild<ChannelsFragmentScreen.KStreamItem> {
                            streamNameText.hasText("#Subscription stream")
                        }
                    }
                }
            }

            step("Смахиваем от правого края к левому") {
                flakySafely {
                    channelsPager.swipeRight()
                }
            }

            step("Проверяем, что стрим без подписки отображается") {
                flakySafely {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(1) {
                        streamsRecycler.firstChild<ChannelsFragmentScreen.KStreamItem> {
                            streamNameText.hasText("#Stream")
                        }
                    }
                }
            }
        }
    }


    @Test
    fun loadTopics() = run {
        AuthorizedTestModulesInjector.inject(ApplicationProvider.getApplicationContext())

        val scenario = launchChannelsFragment()

        ChannelsFragmentScreen {
            step("Проверяем, что стрим отображается") {
                flakySafely {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.firstChild<ChannelsFragmentScreen.KStreamItem> {
                            streamNameText.hasText("#Subscription stream")
                        }
                    }
                }
            }
            step("Нажимаем на стрим") {
                flakySafely {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.firstChild<ChannelsFragmentScreen.KStreamItem> {
                            streamNameText.click()
                        }
                    }
                }
            }
            step("Проверяем, что топик отображается") {
                flakySafely {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.childAt<ChannelsFragmentScreen.KTopicItem>(1) {
                            topicNameText.hasText("Topic")
                        }
                    }
                }
            }

        }
    }


    private fun launchChannelsFragment(): FragmentScenario<ChannelListFragment> {
        return launchFragmentInContainer<ChannelListFragment>(themeResId = ru.snowadv.channels_impl.R.style.Theme_ZulipChat_Channels)
    }
}