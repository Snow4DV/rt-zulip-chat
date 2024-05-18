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
import ru.snowadv.chatapp.rule.ActivityTestRule
import ru.snowadv.chatapp.rule.MockDataRule
import ru.snowadv.chatapp.util.ActivityUtils.onLastFragment

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
}