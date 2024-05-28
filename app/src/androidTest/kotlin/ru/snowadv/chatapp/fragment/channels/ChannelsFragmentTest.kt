package ru.snowadv.chatapp.fragment.channels

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.snowadv.channels_presentation.channel_list.ChannelListFragment
import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.chat.ui.ChatFragment
import ru.snowadv.chatapp.di.injector.AuthorizedTestModulesInjector
import ru.snowadv.chatapp.fragment.channels.screen.ChannelsFragmentScreen
import ru.snowadv.chatapp.rule.FragmentTestRule

@RunWith(AndroidJUnit4::class)
internal class ChannelsFragmentTest : TestCase() {


    @get:Rule
    val fragmentRule = FragmentTestRule(
        fragmentClass = ChannelListFragment::class.java,
        themeResId = ru.snowadv.channels_presentation.R.style.Theme_ZulipChat_Channels,
    )

    @Test
    fun streamsShowUp() = run {
        ChannelsFragmentScreen {
            step("Проверяем, что стрим с подпиской отображается") {
                flakySafely(intervalMs = 200) {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.firstChild<ChannelsFragmentScreen.KStreamItem> {
                            streamNameText.hasText("#${fragmentRule.mockDataRule.data.subscriptionsDto.subscriptions.first().name}")
                        }
                    }
                }
            }

            step("Смахиваем от правого края к левому") {
                flakySafely(intervalMs = 200) {
                    channelsPager.swipeRight()
                }
            }

            step("Проверяем, что стрим без подписки отображается") {
                flakySafely(intervalMs = 200) {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(1) {
                        streamsRecycler.firstChild<ChannelsFragmentScreen.KStreamItem> {
                            streamNameText.hasText("#${fragmentRule.mockDataRule.data.streamsDto.streams.first().name}")
                        }
                    }
                }
            }
        }
    }


    @Test
    fun loadTopics() = run {
        ChannelsFragmentScreen {
            step("Проверяем, что стрим отображается") {
                flakySafely(intervalMs = 200) {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.childAt<ChannelsFragmentScreen.KStreamItem>(0) {
                            streamNameText.hasText("#Subscription stream")
                        }
                    }
                }
            }
            step("Нажимаем на стрим") {
                flakySafely(intervalMs = 200) {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.firstChild<ChannelsFragmentScreen.KStreamItem> {
                            expandButton.click()
                        }
                    }
                }
            }
            step("Проверяем, что топик отображается") {
                flakySafely(intervalMs = 200) {
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
        return launchFragmentInContainer<ChannelListFragment>(themeResId = ru.snowadv.channels_presentation.R.style.Theme_ZulipChat_Channels)
    }
}