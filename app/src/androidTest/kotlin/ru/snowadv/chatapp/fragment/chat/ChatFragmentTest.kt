package ru.snowadv.chatapp.fragment.chat

import android.content.Context
import androidx.core.os.bundleOf
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.chat.ui.ChatFragment
import ru.snowadv.chatapp.fragment.chat.screen.ChatFragmentScreen
import ru.snowadv.chatapp.data.MockData
import ru.snowadv.chatapp.rule.FragmentTestRule

@RunWith(AndroidJUnit4::class)
internal class ChatFragmentTest : TestCase() {
    @get:Rule
    val fragmentTestRule = FragmentTestRule(
        fragmentClass = ChatFragment::class.java,
        themeResId = R.style.Theme_ZulipChat_Chat,
        fragmentArgs = bundleOf(
            ChatFragment.ARG_STREAM_NAME_KEY to "general",
            ChatFragment.ARG_STREAM_ID_KEY to 1L,
            ChatFragment.ARG_TOPIC_NAME_KEY to "testing",
        ),
    )
    private val mockData get() = fragmentTestRule.mockDataRule.data

    @Test
    fun messagesShowUp() = run {
        ChatFragmentScreen {
            step("Проверяем, что отображаются название топика и название стрима") {
                streamTitle {
                    hasText("#general")
                }
                topicTitle {
                    hasText("Topic: #testing")
                }
            }
            step("Проверяем, что был вызов метода получения сообщений") {
                flakySafely(intervalMs = 200) {
                    fragmentTestRule.wiremockRule.wiremock.verify(
                        WireMock.getRequestedFor(
                            urlPathMatching("/api/v1/messages.*")
                        )
                    )
                }
            }

            flakySafely(intervalMs = 200) {
                step("Проверяем, что загрузка завершилась") {
                    progressBar {
                        isGone()
                    }
                }
            }

            flakySafely(intervalMs = 200) {
                step("Проверяем, что не отображаются сообщения об ошибке/о загрузке из кэша") {
                    states.forEach {
                        it.doesNotExist()
                    }
                }

            }

            flakySafely(intervalMs = 200) {
                step("Проверяем, что показываются все сообщения") {
                    listOf(2, 3, 4, 5, 7).forEachIndexed { index, recyclerIndex ->
                        messagesRecycler.childAt<ChatFragmentScreen.KIncomingMessageItem>(
                            recyclerIndex
                        ) {
                            usernameText {
                                hasText(mockData.messagesDto.messages[index].senderFullName)
                            }
                            content {
                                hasText(mockData.messagesDto.messages[index].content)
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
        ChatFragmentScreen {
            step("Проверяем, что загрузка завершилась") {
                flakySafely(intervalMs = 200) {

                    progressBar {
                        isGone()
                    }
                }
            }
            step("Проверяем, что не отображаются сообщения об ошибке/о загрузке из кэша") {
                flakySafely(intervalMs = 200) {
                    states.forEach {
                        it.doesNotExist()
                    }
                }

            }
            step("Вводим текст '123' в поле ввода") {
                messageEditText.typeText("123")
            }
            step("Проверяем, что отображается кнопка отправки сообщения") {
                flakySafely(intervalMs = 200) {
                    sendOrAddAttachmentButton.hasTag(appContext.getString(R.string.send_message_hint))
                }
            }
            step("Отправляем сообщение") {
                flakySafely(intervalMs = 200) {
                    sendOrAddAttachmentButton.perform { click() }
                }
            }
            step("Проверяем, что был вызов метода отправки сообщения с текстом `123`") {
                flakySafely(intervalMs = 200) {

                    fragmentTestRule.wiremockRule.wiremock.verify(
                        WireMock.postRequestedFor(
                            urlMatching("/api/v1/messages.*content=123.*")
                        )
                    )
                }
            }
        }
    }

    @Test
    fun checkIfEventMessageAppeared() = run {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        ChatFragmentScreen {
            step("Проверяем, что новое сообщение содержит текст `123`") {
                flakySafely(intervalMs = 200) {
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
    fun addReaction() = run {
        ChatFragmentScreen {
            step("Проверяем, что загрузка завершилась") {
                flakySafely(intervalMs = 200) {
                    progressBar {
                        isGone()
                    }
                }
            }

            step("Проверяем, что не отображаются сообщения об ошибке/о загрузке из кэша") {
                flakySafely(intervalMs = 200) {
                    states.forEach {
                        it.doesNotExist()
                    }
                }

            }

            step("Проверяем, что у последнего сообщения отображается реакция") {
                flakySafely(intervalMs = 200) {
                    messagesRecycler.lastChild<ChatFragmentScreen.KIncomingMessageItem>() {
                        twoVotesReaction {
                            hasEmojiCode("1f641")
                        }
                    }

                }
            }

            flakySafely(intervalMs = 200) {
                step("Добавляем реакцию") {
                    messagesRecycler.lastChild<ChatFragmentScreen.KIncomingMessageItem>() {
                        twoVotesReaction {
                            perform { click() }
                        }
                    }

                }
            }

            flakySafely(intervalMs = 200) {
                step("Проверяем, что отправка реакции завершилась") {
                    progressBar {
                        isGone()
                    }
                }
            }

            step("Проверяем, что был вызов метода добавления реакции") {
                flakySafely(intervalMs = 200) {
                    fragmentTestRule.wiremockRule.wiremock.verify(
                        WireMock.postRequestedFor(
                            urlMatching(".*/api/v1/messages.*/reactions.*")
                        )
                    )
                }
            }
        }
    }


    @Test
    fun checkIfEventReactionsAreShown() = run {
        ChatFragmentScreen {
            step("Проверяем, что у последнего сообщения отображается реакция с двумя голосами") {
                flakySafely(intervalMs = 200) {
                    messagesRecycler.lastChild<ChatFragmentScreen.KIncomingMessageItem>() {
                        twoVotesReaction {
                            hasEmojiCode("1f641")
                        }
                    }
                }
            }

            step("Проверяем, что у последнего сообщения отображается реакция с одним голосом, добавленная текущим пользователем") {
                flakySafely(intervalMs = 200) {
                    messagesRecycler.lastChild<ChatFragmentScreen.KIncomingMessageItem>() {
                        oneVoteReaction {
                            hasEmojiCode("1f917")
                            isSelected()
                        }
                    }
                }
            }
        }
    }


    @Test
    fun removeReaction() = run {
        ChatFragmentScreen {
            step("Проверяем, что загрузка завершилась") {
                flakySafely(intervalMs = 200) {
                    progressBar {
                        isGone()
                    }
                }
            }

            step("Проверяем, что не отображаются сообщения об ошибке/о загрузке из кэша") {
                flakySafely(intervalMs = 200) {
                    states.forEach {
                        it.doesNotExist()
                    }
                }

            }

            step("Проверяем, что у последнего сообщения отображается реакция") {
                flakySafely(intervalMs = 200) {
                    messagesRecycler.lastChild<ChatFragmentScreen.KIncomingMessageItem>() {
                        selectedReaction {
                            hasEmojiCode("1f917")
                        }
                    }

                }
            }

            step("Удаляем реакцию") {
                flakySafely(intervalMs = 200) {
                    messagesRecycler.lastChild<ChatFragmentScreen.KIncomingMessageItem>() {
                        selectedReaction {
                            perform { click() }
                        }
                    }

                }
            }

            step("Проверяем, что был вызов метода удаления реакции") {
                flakySafely(intervalMs = 200) {
                    fragmentTestRule.wiremockRule.wiremock.verify(
                        WireMock.deleteRequestedFor(
                            urlMatching(".*/api/v1/messages.*/reactions.*")
                        )
                    )
                }
            }
        }
    }
}