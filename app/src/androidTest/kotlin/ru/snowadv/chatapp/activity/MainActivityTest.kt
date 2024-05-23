package ru.snowadv.chatapp.activity

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tomakehurst.wiremock.client.WireMock
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.chat.ui.ChatFragment
import ru.snowadv.chat_presentation.chat.ui.ChatFragment.Companion.ARG_STREAM_NAME_KEY
import ru.snowadv.chat_presentation.chat.ui.ChatFragment.Companion.ARG_TOPIC_NAME_KEY
import ru.snowadv.chatapp.fragment.channels.screen.ChannelsFragmentScreen
import ru.snowadv.chatapp.fragment.chat.screen.ChatFragmentScreen
import ru.snowadv.chatapp.fragment.home.screen.HomeFragmentScreen
import ru.snowadv.chatapp.fragment.people.screen.PeopleFragmentScreen
import ru.snowadv.chatapp.fragment.profile.screen.ProfileFragmentScreen
import ru.snowadv.chatapp.rule.ActivityTestRule
import ru.snowadv.chatapp.util.ActivityUtils.onLastFragment
import ru.snowadv.profile_presentation.ui.ProfileFragment

@RunWith(AndroidJUnit4::class)
internal class MainActivityTest : TestCase() {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)
    private val mockData get() = activityTestRule.mockDataRule.data

    @Test
    fun sendMessage() = run {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        step("Откроем список топиков в стриме") {
            ChannelsFragmentScreen {
                flakySafely(intervalMs = 200) {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.childAt<ChannelsFragmentScreen.KStreamItem>(0) {
                            expandButton.click()
                        }
                    }
                }
            }
        }
        step("Откроем сообщения в топике") {
            flakySafely(intervalMs = 200) {
                ChannelsFragmentScreen {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.childAt<ChannelsFragmentScreen.KTopicItem>(1) {
                            topicNameText.click()
                        }
                    }
                }
            }
        }
        step("Вводим текст '123' в поле ввода") {
            ChatFragmentScreen {
                messageEditText.replaceText("123")

            }
        }
        step("Проверяем, что был введен текст `123` в поле ввода") {
            ChatFragmentScreen {
                messageEditText.hasText("123")
            }
        }
        step("Проверяем, что отображается кнопка отправки сообщения") {
            flakySafely(intervalMs = 200) {
                ChatFragmentScreen {
                    sendOrAddAttachmentButton.hasTag(appContext.getString(R.string.send_message_hint))
                }
            }
        }
        step("Отправляем сообщение") {
            flakySafely(intervalMs = 200) {
                ChatFragmentScreen {
                    sendOrAddAttachmentButton.perform { click() }
                }
            }
        }
        step("Проверяем, что был вызов метода отправки сообщения") {
            flakySafely(intervalMs = 200) {
                activityTestRule.wiremockTestRule.wiremock.verify(
                    WireMock.postRequestedFor(
                        WireMock.urlPathMatching("/api/v1/messages.*")
                    )
                )
            }
        }
        step("Проверяем, что последнее сообщение содержит текст `123`") {
            flakySafely(intervalMs = 200) {
                ChatFragmentScreen {
                    messagesRecycler.childAt<ChatFragmentScreen.KOutgoingMessageItem>(
                        messagesRecycler.getSize() - 1
                    ) {
                        content {
                            hasText("123")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun openTopic() = run {
        step("Откроем список топиков в стриме") {
            flakySafely(intervalMs = 200) {
                ChannelsFragmentScreen {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.childAt<ChannelsFragmentScreen.KStreamItem>(0) {
                            expandButton.click()
                        }
                    }
                }
            }
        }
        step("Откроем сообщения в топике") {
            flakySafely(intervalMs = 200) {
                ChannelsFragmentScreen {
                    channelsPager.childAt<ChannelsFragmentScreen.KStreamsRecyclerItem>(0) {
                        streamsRecycler.childAt<ChannelsFragmentScreen.KTopicItem>(1) {
                            topicNameText.click()
                        }
                    }
                }
            }
        }
        step("Проверяем, что произошел переход на экран чата с правильными аргументами") {
            flakySafely(intervalMs = 200) {
                activityTestRule.onLastFragment {
                    Assert.assertTrue(
                        "Expected ChatFragment but $this is running",
                        this is ChatFragment
                    )
                    val streamName = mockData.subscriptionsDto.subscriptions.first().name
                    val topicName = mockData.topicsDto.topics.first().name

                    Assert.assertEquals(streamName, arguments?.getString(ARG_STREAM_NAME_KEY))
                    Assert.assertEquals(topicName, arguments?.getString(ARG_TOPIC_NAME_KEY))
                }
            }
        }
    }

    @Test
    fun testMenu() = run {
        step("Проверим, что отображается экран Channels") {
            flakySafely(intervalMs = 200) {
                ChannelsFragmentScreen {
                    isVisible()
                }
            }
        }
        step("Перейдем на экран People") {
            flakySafely(intervalMs = 200) {
                HomeFragmentScreen {
                    bottomNavigationView.setSelectedItem(ru.snowadv.home_presentation.R.id.action_people)
                    bottomNavigationView.hasSelectedItem(ru.snowadv.home_presentation.R.id.action_people)
                }
            }
        }
        step("Проверим, что отображается экран People") {
            flakySafely(intervalMs = 200) {
                PeopleFragmentScreen {
                    isVisible()
                }
            }
        }
        step("Перейдем на экран Profile") {
            flakySafely(intervalMs = 200) {
                HomeFragmentScreen {
                    bottomNavigationView.setSelectedItem(ru.snowadv.home_presentation.R.id.action_profile)
                    bottomNavigationView.hasSelectedItem(ru.snowadv.home_presentation.R.id.action_profile)
                }
            }
        }
        step("Проверим, что отображается экран Profile") {
            flakySafely(intervalMs = 200) {
                ProfileFragmentScreen {
                    isVisible()
                }
            }
        }
    }

    @Test
    fun openOtherUserProfile() = run {
        step("Перейдем на экран People") {
            flakySafely(intervalMs = 200) {
                HomeFragmentScreen {
                    bottomNavigationView.setSelectedItem(ru.snowadv.home_presentation.R.id.action_people)
                    bottomNavigationView.hasSelectedItem(ru.snowadv.home_presentation.R.id.action_people)
                }
            }
        }
        step("Откроем профиль пользователя") {
            flakySafely(intervalMs = 200) {
                PeopleFragmentScreen {
                    peopleRecycler.childAt<PeopleFragmentScreen.KPeopleItem>(0) {
                        userName.click()
                    }
                }
            }
        }
        step("Убедимся, что профиль отображается корректно") {
            flakySafely(intervalMs = 200) {
                ProfileFragmentScreen {
                    isVisible()
                    userName.hasText(activityTestRule.mockDataRule.data.profile.user.name)
                    userEmail.hasText(activityTestRule.mockDataRule.data.profile.user.email)
                }
            }
        }
    }
}