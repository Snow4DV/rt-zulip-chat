package ru.snowadv.chat_presentation.chat.presentation.elm

import io.kotest.common.runBlocking
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.chat_presentation.chat.presentation.elm.data.ChatActorElmTestData
import ru.snowadv.chat_presentation.chat.presentation.elm.mock.AddReactionUseCaseMock
import ru.snowadv.chat_presentation.chat.presentation.elm.mock.ChatRouterMock
import ru.snowadv.chat_presentation.chat.presentation.elm.mock.GetCurrentMessagesUseCaseMock
import ru.snowadv.chat_presentation.chat.presentation.elm.mock.ListenToChatEventsUseCaseMock
import ru.snowadv.chat_presentation.chat.presentation.elm.mock.LoadMoreMessagesUseCaseMock
import ru.snowadv.chat_presentation.chat.presentation.elm.mock.RemoveReactionUseCaseMock
import ru.snowadv.chat_presentation.chat.presentation.elm.mock.SendFileUseCaseMock
import ru.snowadv.chat_presentation.chat.presentation.elm.mock.SendMessageUseCaseMock
import ru.snowadv.test_utils.exception.DataException
import ru.snowadv.test_utils.util.TestUtils.runBlocking
import ru.snowadv.test_utils.util.TestUtils.runBlockingToList
import java.io.IOException

@RunWith(JUnit4::class)
class ChatActorElmTest : BehaviorSpec({
    with(ChatActorElmTestData()) {
        Given("ChatActorElm and ChatRouterMock") {
            val router = ChatRouterMock()
            val actor = ChatActorElm(
                router = router,
                addReactionUseCase = AddReactionUseCaseMock(),
                removeReactionUseCase = RemoveReactionUseCaseMock(),
                sendMessageUseCase = SendMessageUseCaseMock(),
                getMessagesUseCase = GetCurrentMessagesUseCaseMock(this@with),
                listenToChatEventsUseCase = ListenToChatEventsUseCaseMock(this@with),
                loadMoreMessagesUseCase = LoadMoreMessagesUseCaseMock(this@with),
                sendFileUseCase = SendFileUseCaseMock(),
            )
            When("execute") {
                And("command is LoadInitialMessages") {
                    val command = ChatCommandElm.LoadInitialMessages(
                        streamName = streamName,
                        topicName = topicName,
                    )

                    val expectedLoading = ChatEventElm.Internal.Loading
                    val expectedLoadingWithCache = ChatEventElm.Internal.InitialChatLoaded(cachedMessages.toTestPaginatedMessages(), true)
                    val expectedSuccess = ChatEventElm.Internal.InitialChatLoaded(remoteMessages.toTestPaginatedMessages(), false)
                    val expectedError = ChatEventElm.Internal.Error(DataException(), cachedMessages.toTestPaginatedMessages())

                    Then("should emit expectedLoading, expectedLoadingWithCache, expectedSuccess, expectedError") {
                        actor.execute(command).runBlockingToList() shouldBe listOf(expectedLoading, expectedLoadingWithCache, expectedSuccess, expectedError)
                    }
                }

                And("command is LoadMoreMessages") {
                    val command = ChatCommandElm.LoadMoreMessages(
                        streamName = streamName,
                        topicName = topicName,
                        firstLoadedMessageId = remoteMessages.first().id,
                        includeAnchor = false,
                    )

                    val expectedLoading = ChatEventElm.Internal.PaginationLoading
                    val expectedSuccess = ChatEventElm.Internal.MoreMessagesLoaded(secondPageMessages.toTestPaginatedMessages())
                    val expectedError = ChatEventElm.Internal.PaginationError

                    Then("should emit expectedLoading, expectedSuccess, expectedError") {
                        actor.execute(command).runBlockingToList() shouldBe listOf(expectedLoading, expectedSuccess, expectedError)
                    }
                }

                And("command is ObserveEvents") {
                    val command = ChatCommandElm.ObserveEvents(
                        streamName = streamName,
                        topicName = topicName,
                        isRestart = false,
                        queueProps = null,
                    )

                    Then("should emit all elmServerEvents") {
                        actor.execute(command).runBlockingToList() shouldBe elmServerEvents
                    }
                }

                And("command is AddChosenReaction") {
                    val command = ChatCommandElm.AddChosenReaction(
                        messageId = 1,
                        reactionName = "smile",
                    )

                    val expectedRetryEvent =ChatEventElm.Ui.AddChosenReaction(
                        messageId = 1,
                        reactionName = "smile",
                    )

                    val expectedLoading = ChatEventElm.Internal.ChangingReaction
                    val expectedSuccess = ChatEventElm.Internal.ChangingReactionError(expectedRetryEvent)
                    val expectedError = ChatEventElm.Internal.ReactionChanged

                    Then("should emit expectedLoading, expectedError, expectedSuccess") {
                        actor.execute(command).runBlockingToList() shouldBe listOf(expectedLoading, expectedError, expectedSuccess)
                    }
                }

                And("command is RemoveReaction") {
                    val command = ChatCommandElm.RemoveReaction(
                        messageId = 1,
                        reactionName = "smile",
                    )

                    val expectedRetryEvent =ChatEventElm.Ui.RemoveReaction(
                        messageId = 1,
                        reactionName = "smile",
                    )

                    val expectedLoading = ChatEventElm.Internal.ChangingReaction
                    val expectedSuccess = ChatEventElm.Internal.ChangingReactionError(expectedRetryEvent)
                    val expectedError = ChatEventElm.Internal.ReactionChanged

                    Then("should emit expectedLoading, expectedError, expectedSuccess") {
                        actor.execute(command).runBlockingToList() shouldBe listOf(expectedLoading, expectedError, expectedSuccess)
                    }
                }

                And("command is GoToProfile") {
                    val command = ChatCommandElm.GoToProfile(
                        profileId = 1,
                    )

                    Then("router commands shouldContain openProfile(1)") {
                        actor.execute(command).runBlocking()
                        router.commands shouldContain "openProfile(1)"
                    }
                }

                And("command is GoBack") {
                    val command = ChatCommandElm.GoBack

                    Then("router commands shouldBe [goBack]") {
                        actor.execute(command).runBlocking()
                        router.commands shouldContain "goBack()"
                    }
                }

                And("command is SendMessage") {
                    val command = ChatCommandElm.SendMessage(
                        streamName = streamName,
                        topicName = topicName,
                        text = sampleText,
                    )

                    val expectedLoading = ChatEventElm.Internal.SendingMessage
                    val expectedSuccess = ChatEventElm.Internal.MessageSent
                    val expectedError = ChatEventElm.Internal.SendingMessageError

                    Then("should emit expectedLoading, expectedSuccess, expectedError") {
                        actor.execute(command).runBlockingToList() shouldBe listOf(expectedLoading, expectedSuccess, expectedError)
                    }
                }

                And("command is AddAttachment") {
                    val command = ChatCommandElm.AddAttachment(
                        streamName = streamName,
                        topicName = topicName,
                        mimeType = "image/png",
                        inputStreamOpener = { null },
                        extension = "png",
                    )

                    val retryEvent = ChatEventElm.Ui.FileWasChosen(
                        command.mimeType,
                        command.inputStreamOpener,
                        command.extension,
                    )

                    val expectedLoading = ChatEventElm.Internal.UploadingFile
                    val expectedSuccess = ChatEventElm.Internal.FileUploaded
                    val expectedError = ChatEventElm.Internal.UploadingFileError(retryEvent)

                    Then("should emit expectedLoading, expectedSuccess, expectedError") {
                        actor.execute(command).runBlockingToList() shouldBe listOf(expectedLoading, expectedSuccess, expectedError)
                    }
                }
            }
        }
    }
})

private fun List<ChatMessage>.toTestPaginatedMessages(): ChatPaginatedMessages {
    return ChatPaginatedMessages(
        messages = this,
        foundAnchor = true,
        foundOldest = true,
        foundNewest = true,
    )
}

