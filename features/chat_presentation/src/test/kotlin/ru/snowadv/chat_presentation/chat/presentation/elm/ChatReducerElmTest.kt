package ru.snowadv.chat_presentation.chat.presentation.elm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ru.snowadv.chat_domain_api.model.ChatPaginationStatus
import ru.snowadv.chat_presentation.chat.presentation.elm.data.ChatReducerElmTestData
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.ScreenState

@RunWith(JUnit4::class)
class ChatReducerElmTest : BehaviorSpec({
    with(ChatReducerElmTestData()) {
        Given("ChatReducerElm") {
            val reducer = ChatReducerElm()
            When("reduce") {
                And("state is initial (resumed)") {
                    // Internal events
                    And("event is Internal.InitialChatLoaded with page 0 and cached false") {
                        val event =
                            ChatEventElm.Internal.InitialChatLoaded(paginatedMessages[0], false)
                        val expectedCommand = ChatCommandElm.ObserveEvents(
                            streamName = streamName,
                            topicName = topicName,
                            isRestart = false,
                            queueProps = null,
                        )
                        val actual = reducer.reduce(event, initialResumedState)

                        Then(
                            "screenState should be success, messages should be expectedMessages, pagination status" +
                                    " should be HasMore, eventQueueData should be null and commands should contain observe"
                        ) {
                            actual.state.screenState shouldBe ScreenState.Success(chunkedMessages[0])
                            actual.state.messages shouldBe chunkedMessages[0]
                            actual.state.paginationStatus shouldBe ChatPaginationStatus.HasMore
                            actual.state.eventQueueData shouldBe null
                            actual.commands shouldContain expectedCommand
                        }
                    }

                    And("event is Internal.InitialChatLoaded with cached is true") {
                        val event =
                            ChatEventElm.Internal.InitialChatLoaded(paginatedMessages[0], true)
                        val actual = reducer.reduce(event, initialResumedState)

                        Then(
                            "screenState should be success, messages should be expectedMessages, pagination status" +
                                    " should be HasMore and eventQueueData should be null"
                        ) {
                            actual.state.screenState shouldBe ScreenState.Success(chunkedMessages[0])
                            actual.state.messages shouldBe chunkedMessages[0]
                            actual.state.paginationStatus shouldBe ChatPaginationStatus.None
                            actual.state.eventQueueData shouldBe null
                            actual.commands.shouldBeEmpty()
                        }
                    }

                    And("event is Internal.Error and no cache") {
                        val event = ChatEventElm.Internal.Error(internetErrorException, null)
                        val actual = reducer.reduce(event, initialResumedState)

                        Then(
                            "screenState should be error, messages are empty, pagination status" +
                                    " should be None and eventQueueData should be null"
                        ) {
                            actual.state.screenState shouldBe ScreenState.Error(
                                internetErrorException,
                                null
                            )
                            actual.state.messages.shouldBeEmpty()
                            actual.state.paginationStatus shouldBe ChatPaginationStatus.None
                            actual.state.eventQueueData shouldBe null
                            actual.commands.shouldBeEmpty()
                        }
                    }

                    And("event is Internal.ChangingReaction") {
                        val event = ChatEventElm.Internal.ChangingReaction
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("changingReaction should be true") {
                            actual.state.changingReaction shouldBe true
                        }
                    }

                    And("event is Internal.Loading") {
                        val event = ChatEventElm.Internal.Loading
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("screenState should be Loading") {
                            actual.state.screenState shouldBe ScreenState.Loading()
                        }
                    }

                    And("event is Internal.MessageSent") {
                        val event = ChatEventElm.Internal.Loading
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("sendingMessage should be false and messageField should be empty") {
                            actual.state.sendingMessage shouldBe false
                            actual.state.messageField.shouldBeEmpty()
                        }
                    }

                    And("event is Internal.MoreMessagesLoaded") {
                        val event = ChatEventElm.Internal.MoreMessagesLoaded(paginatedMessages[0])
                        val actual = reducer.reduce(event, initialResumedState)

                        // Can't paginate if there's no data loaded
                        Then("screenState should be Loading") {
                            actual.state.screenState shouldBe ScreenState.Loading()
                            actual.state.messages.shouldBeEmpty()
                            actual.state.paginationStatus shouldBe ChatPaginationStatus.None
                        }
                    }

                    And("event is Internal.ReactionChanged") {
                        val event = ChatEventElm.Internal.SendingMessage
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("changingReaction should be false") {
                            actual.state.changingReaction shouldBe false
                        }
                    }

                    And("event is Internal.SendingMessage") {
                        val event = ChatEventElm.Internal.SendingMessage
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("sendingMessage should be true") {
                            actual.state.sendingMessage shouldBe true
                        }
                    }

                    And("event is Internal.PaginationError") {
                        val event = ChatEventElm.Internal.PaginationError
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("paginationStatus should be Error") {
                            actual.state.paginationStatus shouldBe ChatPaginationStatus.Error
                        }
                    }

                    And("event is Internal.PaginationLoading") {
                        val event = ChatEventElm.Internal.PaginationLoading
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("paginationStatus should be Loading") {
                            actual.state.paginationStatus shouldBe ChatPaginationStatus.Loading
                        }
                    }

                    And("event is Internal.ChangingReactionError") {
                        val retryEvent =
                            ChatEventElm.Ui.AddChosenReaction(sampleMessageId, sampleEmojiName)
                        val event = ChatEventElm.Internal.ChangingReactionError(retryEvent)
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("effects contains ShowActionErrorWithRetry and changingReaction should be false") {
                            actual.effects shouldContain ChatEffectElm.ShowActionErrorWithRetry(
                                retryEvent
                            )
                            actual.state.changingReaction shouldBe false
                        }
                    }

                    And("event is Internal.SendingMessageError") {
                        val event = ChatEventElm.Internal.SendingMessageError
                        val retryEvent = ChatEventElm.Ui.SendMessageAddAttachmentButtonClicked
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("effects contains ShowActionErrorWithRetry and sendingMessage should be false") {
                            actual.effects shouldContain ChatEffectElm.ShowActionErrorWithRetry(
                                retryEvent
                            )
                            actual.state.sendingMessage shouldBe false
                        }
                    }

                    And("event is Internal.FileUploaded") {
                        val event = ChatEventElm.Internal.FileUploaded
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("uploadingFile should be false") {
                            actual.state.uploadingFile shouldBe false
                        }
                    }

                    And("event is Internal.UploadingFile") {
                        val event = ChatEventElm.Internal.UploadingFile
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("uploadingFile should be true") {
                            actual.state.uploadingFile shouldBe true
                        }
                    }

                    And("event is Internal.UploadingFileError") {
                        val retryEvent = ChatEventElm.Ui.FileWasChosen(
                            mimeType = "image/png",
                            inputStreamOpener = { null },
                            extension = "png",
                        )
                        val event = ChatEventElm.Internal.UploadingFileError(retryEvent)
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("effects should contain ShowActionErrorWithRetry and uploadingFile should be false") {
                            actual.effects shouldContain ChatEffectElm.ShowActionErrorWithRetry(
                                retryEvent
                            )
                            actual.state.uploadingFile shouldBe false
                        }
                    }

                    // Ui events
                    And("event is Ui.AddChosenReaction") {
                        val event = ChatEventElm.Ui.AddChosenReaction(1, sampleEmojiName)
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("commands should contain AddChosenReaction") {
                            actual.commands shouldContain ChatCommandElm.AddChosenReaction(
                                1,
                                sampleEmojiName
                            )
                        }
                    }

                    And("state's changingReaction is true") {
                        val state = initialResumedState.copy(changingReaction = true)
                        And("event is Ui.AddChosenReaction") {
                            val event =
                                ChatEventElm.Ui.AddChosenReaction(sampleMessageId, sampleEmojiName)
                            val actual = reducer.reduce(event, state)

                            Then("commands should be empty") {
                                actual.commands.shouldBeEmpty()
                            }
                        }
                    }

                    And("event is Ui.AddReactionClicked") {
                        val event = ChatEventElm.Ui.AddReactionClicked(messageId = sampleMessageId)
                        val actual = reducer.reduce(event, initialResumedState)
                        val expectedEffect = ChatEffectElm.OpenReactionChooser(
                            destMessageId = sampleMessageId
                        )

                        Then("effects should contain $expectedEffect") {
                            actual.effects shouldContain expectedEffect
                        }
                    }

                    And("event is Ui.GoBackClicked") {
                        val event = ChatEventElm.Ui.GoBackClicked
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("commands should contain GoBack") {
                            actual.commands shouldContain ChatCommandElm.GoBack
                        }
                    }

                    And("event is Ui.GoToProfileClicked") {
                        val event = ChatEventElm.Ui.GoToProfileClicked(profileId = sampleUserId)
                        val actual = reducer.reduce(event, initialResumedState)

                        val expectedCommand = ChatCommandElm.GoToProfile(profileId = sampleUserId)

                        Then("commands should contain $expectedCommand") {
                            actual.commands shouldContain expectedCommand
                        }
                    }

                    And("event is Ui.MessageFieldChanged to empty") {
                        val event = ChatEventElm.Ui.MessageFieldChanged("")
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("state messageField should be empty and actionButtonType should be ADD_ATTACHMENT") {
                            actual.state.messageField.shouldBeEmpty()
                            actual.state.actionButtonType shouldBe ChatStateElm.ActionButtonType.ADD_ATTACHMENT
                        }
                    }

                    And("event is Ui.MessageFieldChanged to '$sampleText'") {
                        val event = ChatEventElm.Ui.MessageFieldChanged(sampleText)
                        val actual = reducer.reduce(event, initialResumedState)

                        Then("state messageField should be $sampleText and actionButtonType should be SEND_MESSAGE") {
                            actual.state.messageField shouldBe sampleText
                            actual.state.actionButtonType shouldBe ChatStateElm.ActionButtonType.SEND_MESSAGE
                        }
                    }

                    And("event is Ui.MessageLongClicked") {
                        val event = ChatEventElm.Ui.MessageLongClicked(
                            messageId = sampleMessageId,
                            userId = sampleUserId,
                        )
                        val actual = reducer.reduce(event, initialResumedState)
                        val expectedEffect = ChatEffectElm.OpenMessageActionsChooser(
                            messageId = sampleMessageId,
                            userId = sampleUserId,
                        )

                        Then("effects should contain $expectedEffect") {
                            actual.effects shouldContain expectedEffect
                        }
                    }

                    And("state's ChatPaginationStatus is HasMore and has messages") {
                        val state = initialResumedState.copy(
                            paginationStatus = ChatPaginationStatus.HasMore,
                            messages = chunkedMessages[0],
                            screenState = ScreenState.Success(chunkedMessages[0])
                        )
                        And("event is Ui.PaginationLoadMore") {
                            val event = ChatEventElm.Ui.PaginationLoadMore
                            val actual = reducer.reduce(event, state)

                            val expectedCommand = ChatCommandElm.LoadMoreMessages(
                                streamName = streamName,
                                topicName = topicName,
                                firstLoadedMessageId = chunkedMessages[0][0].id,
                                includeAnchor = false,
                            )

                            Then("state's paginationStatus should be Loading and commands should contain $expectedCommand") {
                                actual.state.paginationStatus shouldBe ChatPaginationStatus.Loading
                                actual.commands shouldContain expectedCommand
                            }
                        }
                        And("event is Ui.ScrolledToNTopMessages") {
                            val event = ChatEventElm.Ui.ScrolledToNTopMessages
                            val actual = reducer.reduce(event, state)

                            val expectedCommand = ChatCommandElm.LoadMoreMessages(
                                streamName = streamName,
                                topicName = topicName,
                                firstLoadedMessageId = chunkedMessages[0][0].id,
                                includeAnchor = false,
                            )

                            Then("state's paginationStatus should be Loading and commands should contain $expectedCommand") {
                                actual.state.paginationStatus shouldBe ChatPaginationStatus.Loading
                                actual.commands shouldContain expectedCommand
                            }
                        }
                    }

                    And("state's ChatPaginationStatus is HasMore and has no messages") {
                        val state = initialResumedState.copy(
                            paginationStatus = ChatPaginationStatus.HasMore,
                            screenState = ScreenState.Success(emptyList())
                        )
                        And("event is Ui.PaginationLoadMore") {
                            val event = ChatEventElm.Ui.PaginationLoadMore
                            val actual = reducer.reduce(event, state)

                            val expectedCommand = ChatCommandElm.LoadMoreMessages(
                                streamName = streamName,
                                topicName = topicName,
                                firstLoadedMessageId = null,
                                includeAnchor = true,
                            )

                            Then("state's paginationStatus should be Loading and commands should contain $expectedCommand") {
                                actual.state.paginationStatus shouldBe ChatPaginationStatus.Loading
                                actual.commands shouldContain expectedCommand
                            }
                        }
                        And("event is Ui.ScrolledToNTopMessages") {
                            val event = ChatEventElm.Ui.ScrolledToNTopMessages
                            val actual = reducer.reduce(event, state)

                            val expectedCommand = ChatCommandElm.LoadMoreMessages(
                                streamName = streamName,
                                topicName = topicName,
                                firstLoadedMessageId = null,
                                includeAnchor = true,
                            )

                            Then("state's paginationStatus should be Loading and commands should contain $expectedCommand") {
                                actual.state.paginationStatus shouldBe ChatPaginationStatus.Loading
                                actual.commands shouldContain expectedCommand
                            }
                        }
                    }

                    And("event is Ui.ReloadClicked") {
                        val event = ChatEventElm.Ui.ReloadClicked
                        val actual = reducer.reduce(event, initialResumedState)

                        val expectedCommand = ChatCommandElm.LoadInitialMessages(
                            streamName = streamName,
                            topicName = topicName,
                        )

                        Then("commands should contain $expectedCommand") {
                            actual.commands shouldContain expectedCommand
                        }
                    }

                    And("event is Ui.RemoveReaction") {
                        val event = ChatEventElm.Ui.RemoveReaction(
                            messageId = sampleMessageId,
                            reactionName = sampleEmojiName,
                        )
                        val actual = reducer.reduce(event, initialResumedState)

                        val expectedCommand = ChatCommandElm.RemoveReaction(
                            messageId = sampleMessageId,
                            reactionName = sampleEmojiName,
                        )

                        Then("commands should contain $expectedCommand") {
                            actual.commands shouldContain expectedCommand
                        }
                    }

                    And("event is Ui.SendMessageAddAttachmentButtonClicked") {
                        val event = ChatEventElm.Ui.SendMessageAddAttachmentButtonClicked
                        val actual = reducer.reduce(event, initialResumedState)

                        val expectedEffect = ChatEffectElm.OpenFileChooser

                        Then("effects should contain $expectedEffect") {
                            actual.effects shouldContain expectedEffect
                        }
                    }

                    And("state message is '$sampleText' and action button type is SEND_MESSAGE") {
                        val state = initialResumedState.copy(
                            actionButtonType = ChatStateElm.ActionButtonType.SEND_MESSAGE,
                            messageField = sampleText
                        )

                        And("event is Ui.SendMessageAddAttachmentButtonClicked") {
                            val event = ChatEventElm.Ui.SendMessageAddAttachmentButtonClicked
                            val actual = reducer.reduce(event, state)

                            val expectedCommand = ChatCommandElm.SendMessage(
                                streamName = streamName,
                                topicName = topicName,
                                text = sampleText,
                            )

                            Then("commands should contain $expectedCommand") {
                                actual.commands shouldContain expectedCommand
                            }
                        }
                    }

                    And("event is Ui.Init") {
                        val event = ChatEventElm.Ui.Init
                        val actual = reducer.reduce(event, initialResumedState)

                        val expectedCommand = ChatCommandElm.LoadInitialMessages(
                            streamName = streamName,
                            topicName = topicName,
                        )

                        Then("state should contain $expectedCommand") {
                            actual.commands shouldContain expectedCommand
                        }
                    }

                    And("state resumed is false") {
                        val state = initialResumedState.copy(resumed = false)

                        And("event is Ui.Resumed") {
                            val event = ChatEventElm.Ui.Resumed
                            val actual = reducer.reduce(event, state)

                            val expectedCommand = ChatCommandElm.ObserveEvents(
                                streamName = streamName,
                                topicName = topicName,
                                isRestart = false,
                                queueProps = null,
                            )

                            Then("state resumed should be true and commands should contain $expectedCommand") {
                                actual.state.resumed shouldBe true
                            }
                        }
                    }
                }


                And("state is first page loaded, eventQueueData is null and paginationStatus is HasMore") {
                    And("event is Internal.ServerEvent.EventQueueRegistered") {
                        val expectedQueueId = "0"
                        val expectedEventId = -1L
                        val expectedTimeoutSeconds = 10_000

                        val event = ChatEventElm.Internal.ServerEvent.EventQueueRegistered(
                            queueId = expectedQueueId,
                            eventId = expectedEventId,
                            timeoutSeconds = expectedTimeoutSeconds,
                        )

                        val expectedQueueProps = EventQueueProperties(
                            queueId = expectedQueueId,
                            timeoutSeconds = expectedTimeoutSeconds,
                            lastEventId = expectedEventId,
                        )
                        val expectedCommand = ChatCommandElm.ObserveEvents(
                            streamName = streamName,
                            topicName = topicName,
                            isRestart = false,
                            queueProps = expectedQueueProps,
                        )

                        val actual = reducer.reduce(event, firstPageStateNullEventQueueData)

                        Then("state eventQueueProps is $expectedQueueProps and commands contains $expectedCommand") {
                            actual.state.eventQueueData shouldBe expectedQueueProps
                            actual.commands shouldContain expectedCommand
                        }
                    }

                    And("event is Internal.ServerEvent.EventQueueFailed") {
                        val expectedQueueId = "0"
                        val expectedEventId = -1L

                        val event = ChatEventElm.Internal.ServerEvent.EventQueueFailed(
                            queueId = expectedQueueId,
                            eventId = expectedEventId,
                            recreateQueue = true,
                        )

                        val actual = reducer.reduce(event, firstPageStateNullEventQueueData)

                        Then("eventQueueData should be null and commands should be empty") {
                            actual.state.eventQueueData shouldBe null
                            actual.commands.shouldBeEmpty()
                        }
                    }
                    testServerEvents.forEach { event ->
                        And("event is $event") {
                            // All events from server until the queue is initialized

                            val actual = reducer.reduce(event, firstPageStateNullEventQueueData)
                            val expectedMessages = firstPageStateNullEventQueueData.messages
                            Then("commands should be empty and messages untouched") {
                                actual.commands.shouldBeEmpty()
                                actual.state.messages shouldBe expectedMessages
                            }
                        }
                    }
                }

                And("state is first page loaded, eventQueueData is not null and paginationStatus is HasMore") {
                    And("event is Internal.ServerEvent.EventQueueFailed") {
                        And("EventQueueFailed.recreateQueue is true") {
                            val event = ChatEventElm.Internal.ServerEvent.EventQueueFailed(
                                queueId = initialEventQueuePropsAfterRegister.queueId,
                                eventId = -1,
                                recreateQueue = true,
                            )

                            val expectedCommand = ChatCommandElm.LoadInitialMessages(
                                streamName = streamName,
                                topicName = topicName,
                            )

                            val actual = reducer.reduce(event, firstPageStateObserveStarted)

                            Then("commands should cointain $expectedCommand") {
                                actual.commands shouldContain expectedCommand
                            }
                        }

                        And("EventQueueFailed.recreateQueue is false") {
                            val event = ChatEventElm.Internal.ServerEvent.EventQueueFailed(
                                queueId = initialEventQueuePropsAfterRegister.queueId,
                                eventId = -1,
                                recreateQueue = false,
                            )

                            val expectedCommand = ChatCommandElm.ObserveEvents(
                                streamName = streamName,
                                topicName = topicName,
                                isRestart = true,
                                queueProps = initialEventQueuePropsAfterRegister,
                            )

                            val actual = reducer.reduce(event, firstPageStateObserveStarted)

                            Then("commands should cointain $expectedCommand") {
                                actual.commands shouldContain expectedCommand
                            }
                        }
                    }

                    And("event is EventQueueRegistered") {
                        val event = ChatEventElm.Internal.ServerEvent.EventQueueRegistered(
                            queueId = initialEventQueuePropsAfterRegister.queueId,
                            eventId = initialEventQueuePropsAfterRegister.lastEventId,
                            timeoutSeconds = initialEventQueuePropsAfterRegister.timeoutSeconds,
                        )
                        val actual = reducer.reduce(event, firstPageStateObserveStarted)

                        // Ignoring register if queue is already registered
                        Then("commands should be empty") {
                            actual.commands.shouldBeEmpty()
                        }
                    }

                    // Check if commands re-observe in case of matching eventId and queueId
                    testServerEvents.forEach { event ->
                        And("event is $event") {
                            val expectedEventQueueData = initialEventQueuePropsAfterRegister.copy(
                                lastEventId = event.eventId,
                            )
                            val expectedCommand = ChatCommandElm.ObserveEvents(
                                streamName = streamName,
                                topicName = topicName,
                                isRestart = false,
                                queueProps = expectedEventQueueData,
                            )

                            val actual = reducer.reduce(event, firstPageStateObserveStarted)

                            Then("state eventQueueData should be $expectedEventQueueData and commands should contain $expectedCommand") {
                                actual.state.eventQueueData shouldBe expectedEventQueueData
                                actual.commands shouldContain expectedCommand
                            }
                        }
                    }

                    And("state queueId is not correct (equals asd-das)") {
                        val expectedQueueData = firstPageStateObserveStarted.eventQueueData?.copy(
                            queueId = "asd-das",
                        )
                        val state = firstPageStateObserveStarted.copy(
                            eventQueueData = expectedQueueData,
                        )

                        // Check if command is ignored if queueId is wrong
                        testServerEvents.forEach { event ->
                            And("event is $event") {
                                val actual = reducer.reduce(event, state)

                                Then("state eventQueueData should be $initialEventQueuePropsAfterRegister and commands should be empty") {
                                    actual.state.eventQueueData shouldBe expectedQueueData
                                    actual.commands.shouldBeEmpty()
                                }
                            }
                        }
                    }

                    And("event is Internal.ServerEvent.MessageDeleted") {
                        val event = ChatEventElm.Internal.ServerEvent.MessageDeleted(
                            queueId = initialEventQueuePropsAfterRegister.queueId,
                            eventId = initialEventQueuePropsAfterRegister.lastEventId + 1,
                            messageId = firstPageMessages[0].id,
                        )

                        val expectedMessages = firstPageMessages - firstPageMessages[0]
                        val expectedScreenState = ScreenState.Success(expectedMessages)

                        val actual = reducer.reduce(event, firstPageStateObserveStarted)
                        Then("messages should not contain deleted message and screenState should not contain deleted message") {
                            actual.state.screenState shouldBe expectedScreenState
                            actual.state.messages shouldBe expectedMessages
                        }
                    }

                    And("event is Internal.ServerEvent.MessageUpdated") {
                        val expectedNewContent = "Updated $sampleText"

                        val event = ChatEventElm.Internal.ServerEvent.MessageUpdated(
                            queueId = initialEventQueuePropsAfterRegister.queueId,
                            eventId = initialEventQueuePropsAfterRegister.lastEventId + 1,
                            messageId = firstPageMessages.last().id,
                            newContent = expectedNewContent,
                        )

                        val expectedUpdatedMessage = firstPageMessages.last().copy(
                            content = expectedNewContent,
                        )

                        val expectedMessages = firstPageMessages - firstPageMessages.last() + expectedUpdatedMessage
                        val expectedScreenState = ScreenState.Success(expectedMessages)

                        val actual = reducer.reduce(event, firstPageStateObserveStarted)
                        Then("message should be updated in screen state and messages") {
                            actual.state.screenState shouldBe expectedScreenState
                            actual.state.messages shouldBe expectedMessages
                        }
                    }

                    And("event is Internal.ServerEvent.NewMessage") {
                        val event = ChatEventElm.Internal.ServerEvent.NewMessage(
                            queueId = initialEventQueuePropsAfterRegister.queueId,
                            eventId = initialEventQueuePropsAfterRegister.lastEventId + 1,
                            message = sampleNewMessage,
                        )


                        val expectedMessages = firstPageMessages + sampleNewMessage
                        val expectedScreenState = ScreenState.Success(expectedMessages)

                        val actual = reducer.reduce(event, firstPageStateObserveStarted)
                        Then("message should be added to screen state and messages") {
                            actual.state.screenState shouldBe expectedScreenState
                            actual.state.messages shouldBe expectedMessages
                        }
                    }

                    And("event is Internal.ServerEvent.ReactionAdded by other user") {
                        val event = ChatEventElm.Internal.ServerEvent.ReactionAdded(
                            queueId = initialEventQueuePropsAfterRegister.queueId,
                            eventId = initialEventQueuePropsAfterRegister.lastEventId + 1,
                            messageId = lastMessageInFirstPage.id,
                            emoji = sampleEmoji,
                            currentUserReaction = false,
                        )

                        val existingReaction = lastMessageInFirstPage.reactions.first { it.emojiCode == sampleEmoji.code }

                        val updatedReaction = existingReaction.copy(count = existingReaction.count + 1)

                        val newLastMessage = lastMessageInFirstPage.copy(
                            reactions = (lastMessageInFirstPage.reactions - existingReaction + updatedReaction)
                                .sortedWith(compareBy({-it.count}, {it.name}))
                        )

                        val expectedMessages = firstPageMessages - lastMessageInFirstPage + newLastMessage
                        val expectedScreenState = ScreenState.Success(expectedMessages)

                        val actual = reducer.reduce(event, firstPageStateObserveStarted)
                        Then("reaction should be added to message in screen state and messages and userReacted should be false") {
                            actual.state.messages.last() shouldBe expectedMessages.last()
                            actual.state.screenState shouldBe expectedScreenState
                        }
                    }

                    And("event is Internal.ServerEvent.ReactionAdded by current user") {
                        val event = ChatEventElm.Internal.ServerEvent.ReactionAdded(
                            queueId = initialEventQueuePropsAfterRegister.queueId,
                            eventId = initialEventQueuePropsAfterRegister.lastEventId + 1,
                            messageId = lastMessageInFirstPage.id,
                            emoji = sampleEmoji,
                            currentUserReaction = true,
                        )

                        val existingReaction = lastMessageInFirstPage.reactions.first { it.emojiCode == sampleEmoji.code }

                        val updatedReaction = existingReaction.copy(count = existingReaction.count + 1, userReacted = true)

                        val newLastMessage = lastMessageInFirstPage.copy(
                            reactions = (lastMessageInFirstPage.reactions - existingReaction + updatedReaction)
                                .sortedWith(compareBy({-it.count}, {it.name}))
                        )

                        val expectedMessages = firstPageMessages - lastMessageInFirstPage + newLastMessage
                        val expectedScreenState = ScreenState.Success(expectedMessages)

                        val actual = reducer.reduce(event, firstPageStateObserveStarted)
                        Then("reaction should be added to message in screen state and messages and userReacted in reaction should be true") {
                            actual.state.messages.last() shouldBe expectedMessages.last()
                            actual.state.screenState shouldBe expectedScreenState
                        }
                    }

                    And("event is Internal.ServerEvent.ReactionRemoved by other user") {
                        val event = ChatEventElm.Internal.ServerEvent.ReactionRemoved(
                            queueId = initialEventQueuePropsAfterRegister.queueId,
                            eventId = initialEventQueuePropsAfterRegister.lastEventId + 1,
                            messageId = lastMessageInFirstPage.id,
                            emoji = sampleEmoji,
                            currentUserReaction = false,
                        )

                        val existingReaction = lastMessageInFirstPage.reactions.first { it.emojiCode == sampleEmoji.code }

                        val updatedReaction = existingReaction.copy(count = existingReaction.count - 1)

                        val newLastMessage = lastMessageInFirstPage.copy(
                            reactions = (lastMessageInFirstPage.reactions - existingReaction + updatedReaction)
                                .sortedWith(compareBy({-it.count}, {it.name}))
                        )

                        val expectedMessages = firstPageMessages - lastMessageInFirstPage + newLastMessage
                        val expectedScreenState = ScreenState.Success(expectedMessages)

                        val actual = reducer.reduce(event, firstPageStateObserveStarted)
                        Then("reaction should be added to message in screen state and messages and userReacted should be false") {
                            actual.state.messages.last() shouldBe expectedMessages.last()
                            actual.state.screenState shouldBe expectedScreenState
                        }
                    }

                    And("event is Internal.ServerEvent.ReactionRemoved by other user") {
                        val event = ChatEventElm.Internal.ServerEvent.ReactionRemoved(
                            queueId = initialEventQueuePropsAfterRegister.queueId,
                            eventId = initialEventQueuePropsAfterRegister.lastEventId + 1,
                            messageId = lastMessageInFirstPage.id,
                            emoji = sampleEmoji,
                            currentUserReaction = false,
                        )

                        val existingReaction = lastMessageInFirstPage.reactions.first { it.emojiCode == sampleEmoji.code }

                        val updatedReaction = existingReaction.copy(count = existingReaction.count - 1)

                        val newLastMessage = lastMessageInFirstPage.copy(
                            reactions = (lastMessageInFirstPage.reactions - existingReaction + updatedReaction)
                                .sortedWith(compareBy({-it.count}, {it.name}))
                        )

                        val expectedMessages = firstPageMessages - lastMessageInFirstPage + newLastMessage
                        val expectedScreenState = ScreenState.Success(expectedMessages)

                        val actual = reducer.reduce(event, firstPageStateObserveStarted)
                        Then("reaction should be added to message in screen state and messages and userReacted should be false") {
                            actual.state.messages.last() shouldBe expectedMessages.last()
                            actual.state.screenState shouldBe expectedScreenState
                        }
                    }
                }
            }
        }
    }
})