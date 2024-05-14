package ru.snowadv.chatapp.fragment.chat

import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.chat.ui.ChatFragment
import ru.snowadv.chatapp.fragment.chat.screen.ChatFragmentScreen
import ru.snowadv.chatapp.di.AuthorizedTestModulesInjector
import ru.snowadv.chatapp.rule.WiremockTestRule
import ru.snowadv.chatapp.data.MockData

@RunWith(AndroidJUnit4::class)
internal class ChatFragmentTest : TestCase() {


    @get:Rule
    val wiremockRule = WiremockTestRule()

    @Test
    fun messagesShowUp() = run {
        AuthorizedTestModulesInjector.inject(ApplicationProvider.getApplicationContext())

        val scenario = launchChatFragment("general", "testing")

        ChatFragmentScreen {
            step("Проверяем, что отображаются название топика и название стрима") {
                streamTitle {
                    hasText("#general")
                }
                topicTitle {
                    hasText("Topic: #testing")
                }
            }
            flakySafely {
                step("Проверяем, что был вызов метода получения сообщений") {
                    wiremockRule.wiremock.verify(WireMock.getRequestedFor(urlPathMatching(".*messages.*")))
                }
            }

            flakySafely {

                step("Проверяем, что загрузка завершилась") {
                    progressBar {
                        isGone()
                    }
                }
            }

            flakySafely {
                step("Проверяем, что не отображаются сообщения об ошибке/о загрузке из кэша") {
                    states.forEach {
                        it.doesNotExist()
                    }
                }

            }

            flakySafely {
                step("Проверяем, что показываются все сообщения") {
                    listOf(2, 3, 4, 5, 7).forEachIndexed { index, recyclerIndex ->
                        messagesRecycler.childAt<ChatFragmentScreen.KIncomingMessageItem>(
                            recyclerIndex
                        ) {
                            usernameText {
                                hasText(MockData.initialMessages.messages[index].senderFullName)
                            }
                            content {
                                hasText(MockData.initialMessages.messages[index].content)
                            }
                        }
                    }
                }
            }
        }
    }


    @Test
    fun sendMessage() = run {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        AuthorizedTestModulesInjector.inject(appContext)

        val scenario = launchChatFragment("general", "testing")

        ChatFragmentScreen {

            flakySafely {

                step("Проверяем, что загрузка завершилась") {
                    progressBar {
                        isGone()
                    }
                }
            }

            flakySafely {
                step("Проверяем, что не отображаются сообщения об ошибке/о загрузке из кэша") {
                    states.forEach {
                        it.doesNotExist()
                    }
                }

            }

            step("Вводим текст '123' в поле ввода") {
                messageEditText.typeText("123")
            }

            flakySafely {
                step("Проверяем, что отображается кнопка отправки сообщения") {
                    sendOrAddAttachmentButton.hasTag(appContext.getString(R.string.send_message_hint))
                }
            }
            flakySafely {
                step("Отправляем сообщение") {
                    sendOrAddAttachmentButton.perform { click() }
                }
            }
            flakySafely {
                step("Проверяем, что был вызов метода отправки сообщения") {
                    wiremockRule.wiremock.verify(WireMock.postRequestedFor(urlPathMatching(".*messages.*")))
                }
            }
        }
        flakySafely {
            ChatFragmentScreen {

                step("Проверяем, что последнее сообщение содержит текст `123`") {

                    messagesRecycler.lastChild<ChatFragmentScreen.KOutgoingMessageItem>() {
                        content {
                            hasText("123")
                        }
                    }
                }
            }

        }
    }

    @Test
    fun addReaction() = run {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        AuthorizedTestModulesInjector.inject(appContext)

        val scenario = launchChatFragment("general", "testing")

        ChatFragmentScreen {

            flakySafely {

                step("Проверяем, что загрузка завершилась") {
                    progressBar {
                        isGone()
                    }
                }
            }

            flakySafely {
                step("Проверяем, что не отображаются сообщения об ошибке/о загрузке из кэша") {
                    states.forEach {
                        it.doesNotExist()
                    }
                }

            }

            flakySafely {
                step("Проверяем, что у последнего сообщения отображается реакция") {
                    messagesRecycler.lastChild<ChatFragmentScreen.KIncomingMessageItem>() {
                        oneVoteReaction {
                            hasCount(1)
                            hasEmojiCode("1f917")
                        }
                    }

                }
            }

            flakySafely {
                step("Добавляем реакцию") {
                    messagesRecycler.lastChild<ChatFragmentScreen.KIncomingMessageItem>() {
                        oneVoteReaction {
                            perform { click() }
                        }
                    }

                }
            }
            flakySafely {
                step("Проверяем, что был вызов метода добавления реакции") {
                    wiremockRule.wiremock.verify(WireMock.postRequestedFor(urlPathMatching(".*reactions.*")))
                }
            }
            flakySafely {
                step("Проверяем, что реакция появилась") {
                    messagesRecycler.lastChild<ChatFragmentScreen.KIncomingMessageItem>() {
                        selectedReaction {
                            isDisplayed()
                        }
                    }

                }
            }

            flakySafely {
                step("Проверяем, что отправка реакции завершилась") {
                    progressBar {
                        isGone()
                    }
                }
            }
        }
    }


    private fun launchChatFragment(
        streamName: String,
        topicName: String
    ): FragmentScenario<ChatFragment> {
        return launchFragmentInContainer<ChatFragment>(
            bundleOf(
                ChatFragment.ARG_STREAM_NAME_KEY to streamName,
                ChatFragment.ARG_TOPIC_NAME_KEY to topicName,
            ), ru.snowadv.chat_presentation.R.style.Theme_ZulipChat_Chat
        )
    }
}